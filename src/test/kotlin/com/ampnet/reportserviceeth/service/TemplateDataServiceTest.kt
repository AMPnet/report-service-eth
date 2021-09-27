package com.ampnet.reportserviceeth.service

import com.ampnet.identityservice.proto.UserResponse
import com.ampnet.reportserviceeth.blockchain.TransactionInfo
import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionsServiceRequest
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InvalidRequestException
import com.ampnet.reportserviceeth.exception.ResourceNotFoundException
import com.ampnet.reportserviceeth.persistence.model.Event
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import com.ampnet.reportserviceeth.service.impl.TemplateDataServiceImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.web3j.utils.Convert
import java.math.BigInteger
import java.time.LocalDateTime

class TemplateDataServiceTest : JpaServiceTestBase() {

    private lateinit var testContext: TestContext

    private val templateDataService: TemplateDataService by lazy {
        TemplateDataServiceImpl(
            blockchainService, userService, translationService, eventService, ipfsService, applicationProperties
        )
    }

    @BeforeEach
    fun init() {
        testContext = TestContext()
    }

    @Test
    fun mustGenerateCorrectTxSummary() {
        suppose("gRPC user service will return required data") {
            testContext.user = createUserResponse(userAddress)
            Mockito.`when`(userService.getUser(userAddress))
                .thenReturn(testContext.user)
        }
        suppose("Event service will return transactions for wallet") {
            testContext.events = listOf(
                createEvent(
                    userAddress, projectWallet, TransactionType.RESERVE_INVESTMENT,
                    testContext.reserveInvestment.toString()
                ),
                createEvent(
                    projectWallet, userAddress, TransactionType.CANCEL_INVESTMENT,
                    testContext.cancelInvestment.toString()
                ),
                createEvent(
                    projectWallet, userAddress, TransactionType.COMPLETED_INVESTMENT,
                    testContext.completeInvestment.toString(), "Ox23",
                ),
            )
            testContext.transactionsRequest = TransactionsServiceRequest(
                userAddress, chainId, issuer, PeriodServiceRequest(null, null)
            )
            Mockito.`when`(eventService.getTransactions(testContext.transactionsRequest))
                .thenReturn(testContext.events)
        }
        suppose("Blockchain service will return issuer state") {
            Mockito.`when`(blockchainService.getIssuerCommonState(chainId, issuer))
                .thenReturn(generateIssuerCommonState())
        }
        suppose("File service will return ipfs hash") {
            Mockito.`when`(ipfsService.getLogoUrl(ipfsCid))
                .thenReturn(ipfsCid)
        }

        verify("Template data service can get user transactions") {
            val txSummary = templateDataService.getUserTransactionsData(testContext.transactionsRequest)
            assertThat(txSummary.investments)
                .isEqualTo(BigInteger.valueOf(testContext.reserveInvestment - testContext.cancelInvestment).toMwei())
            assertThat(txSummary.revenueShare).isEqualTo(BigInteger.valueOf(testContext.sharePayout).toMwei())

            val transactions = txSummary.transactions
            assertThat(transactions).hasSize(3)
            val investTx = transactions.first { it.type == TransactionType.RESERVE_INVESTMENT }
            assertThat(investTx.description).isEqualTo(defaultAssetName)
            assertThat(investTx.assetTokenSymbol).isEqualTo(defaultAssetSymbol)
            assertThat(investTx.txDate).isNotBlank
            assertThat(investTx.valueInDollar).isEqualTo(investTx.value.toMwei())

            val cancelInvestmentTx = transactions.first { it.type == TransactionType.CANCEL_INVESTMENT }
            assertThat(cancelInvestmentTx.description).isEqualTo(defaultAssetName)
            assertThat(cancelInvestmentTx.assetTokenSymbol).isEqualTo(defaultAssetSymbol)
            assertThat(cancelInvestmentTx.txDate).isNotBlank
            assertThat(cancelInvestmentTx.valueInDollar).isEqualTo(cancelInvestmentTx.value.toMwei())

            val sharePayoutTx = transactions.first { it.type == TransactionType.COMPLETED_INVESTMENT }
            assertThat(sharePayoutTx.description).isEqualTo(defaultAssetName)
            assertThat(sharePayoutTx.assetTokenSymbol).isEqualTo(defaultAssetSymbol)
            assertThat(sharePayoutTx.txDate).isNotBlank
            assertThat(sharePayoutTx.valueInDollar).isEqualTo(sharePayoutTx.value.toMwei())

            assertThat(txSummary.logo).isEqualTo(applicationProperties.ipfs.ipfsUrl + ipfsCid)
        }
    }

    @Test
    fun mustGenerateCorrectSingleTransactionSummary() {
        suppose("Event service will return event for txHash, userAddress, chainId and issuer") {
            testContext.event = createEvent(
                userAddress, projectWallet, TransactionType.RESERVE_INVESTMENT,
                testContext.reserveInvestment.toString(), txHash,
            )
            testContext.transactionRequest = TransactionServiceRequest(userAddress, txHash, chainId, issuer)
            Mockito.`when`(eventService.getTransaction(testContext.transactionRequest))
                .thenReturn(testContext.event)
        }
        suppose("User service will return userWithInfo") {
            testContext.user = createUserResponse(userAddress)
            Mockito.`when`(userService.getUser(userAddress))
                .thenReturn(testContext.user)
        }

        verify("Template data service can get user transaction") {
            val transaction = TransactionInfo(testContext.event)
            val singleTxSummary = templateDataService.getUserTransactionData(testContext.transactionRequest)
            val tx = singleTxSummary.transaction
            val userInfo = singleTxSummary.userInfo
            assertThat(tx.amount).isEqualTo(transaction.tokenAmount)
            assertThat(tx.value).isEqualTo(transaction.tokenValue)
            assertThat(tx.date).isBeforeOrEqualTo(LocalDateTime.now())
            assertThat(tx.type).isEqualTo(transaction.type)
            assertThat(tx.from).isEqualTo(transaction.from)
            assertThat(tx.to).isEqualTo(transaction.to)
            assertThat(tx.description).isEqualTo(transaction.asset)
            assertThat(tx.assetTokenSymbol).isEqualTo(transaction.assetTokenSymbol)
            assertThat(tx.percentageInProject).isNull()
            assertThat(userInfo.address).isEqualTo(testContext.user.address)
            assertThat(singleTxSummary.logo).isNull()
        }
    }

    @Test
    fun mustGenerateSingleReportInEnglishOnInvalidLanguage() {
        suppose("Event service will return event for txHash, userAddress, chainId and issuer") {
            testContext.event = createEvent(
                userAddress, projectWallet, TransactionType.RESERVE_INVESTMENT,
                testContext.reserveInvestment.toString()
            )
            testContext.transactionRequest = TransactionServiceRequest(userAddress, txHash, chainId, issuer)
            Mockito.`when`(eventService.getTransaction(testContext.transactionRequest))
                .thenReturn(testContext.event)
        }
        suppose("User service will return userWithInfo with invalid language") {
            testContext.user = createUserResponse(userAddress, "invalid_language")
            Mockito.`when`(userService.getUser(userAddress))
                .thenReturn(testContext.user)
        }

        verify("Template data service can get user transaction") {
            val singleTxSummary = templateDataService.getUserTransactionData(testContext.transactionRequest)
            assertThat(singleTxSummary.translations.transactions).isEqualTo("Transactions")
        }
    }

    @Test
    fun mustThrowExceptionIfTransactionDoesNotBelongToUser() {
        suppose("Event service will return null for missing event") {
            testContext.transactionRequest = TransactionServiceRequest(userAddress, txHash, chainId, issuer)
            Mockito.`when`(eventService.getTransaction(testContext.transactionRequest))
                .thenReturn(null)
        }

        verify("Template data service will throw exception if tx doesn't belong to user wallet") {
            val exception = assertThrows<InvalidRequestException> {
                templateDataService.getUserTransactionData(testContext.transactionRequest)
            }
            assertThat(exception.errorCode).isEqualTo(ErrorCode.BLOCKCHAIN_TX_MISSING)
        }
    }

    @Test
    fun mustGenerateTransactionsSummaryReportInEnglishOnInvalidLanguage() {
        suppose("User service will return userWithInfo with invalid language") {
            testContext.user = createUserResponse(userAddress, language = "invalid_language")
            Mockito.`when`(userService.getUser(userAddress))
                .thenReturn(testContext.user)
        }
        suppose("Blockchain service will return transactions for wallet") {
            testContext.events = createEventsFlow()
            testContext.transactionsRequest = TransactionsServiceRequest(
                testContext.user.address, chainId, issuer, PeriodServiceRequest(null, null)
            )
            Mockito.`when`(eventService.getTransactions(testContext.transactionsRequest))
                .thenReturn(testContext.events)
        }

        verify("Template data service can get user transactions") {
            val txSummary = templateDataService.getUserTransactionsData(testContext.transactionsRequest)
            assertThat(txSummary.transactions.first().name).isEqualTo(translationService.getTranslations().reserveInvestment)
        }
    }

    @Test
    fun mustGenerateCorrectUsersAccountsSummary() {
        suppose("User service will return a list of users") {
            testContext.user = createUserResponse(userAddress)
            testContext.secondUser = createUserResponse(secondUserAddress)
            testContext.thirdUser = createUserResponse(thirdUserAddress)
            Mockito.`when`(userService.getUsersForIssuer(IssuerRequest(issuer, chainId)))
                .thenReturn(listOf(testContext.user, testContext.secondUser, testContext.thirdUser))
        }
        suppose("Blockchain service will return transactions for wallets") {
            Mockito.`when`(blockchainService.getTransactions(userAddress, chainId))
                .thenReturn(createTransactionFlow())
            Mockito.`when`(blockchainService.getTransactions(secondUserAddress, chainId))
                .thenReturn(
                    listOf(
                        createTransaction(
                            secondUserAddress, projectWallet, testContext.reserveInvestment.toString(),
                            TransactionType.RESERVE_INVESTMENT
                        )
                    )
                )
            Mockito.`when`(blockchainService.getTransactions(thirdUserAddress, chainId))
                .thenReturn(listOf())
        }

        verify("Template data service can get users accounts summary") {
            val periodRequest = PeriodServiceRequest(null, null)
            val usersAccountsSummary =
                templateDataService.getAllActiveUsersSummaryData(IssuerRequest(issuer, chainId), periodRequest)
            val transactionsSummaryList = usersAccountsSummary.summaries
            assertThat(transactionsSummaryList).hasSize(3)
            assertThat(usersAccountsSummary.logo).isNull()
            transactionsSummaryList.forEach {
                when (it.userInfo.address) {
                    userAddress -> {
                        assertThat(it.investments).isEqualTo(
                            BigInteger.valueOf(testContext.reserveInvestment - testContext.cancelInvestment).toMwei()
                        )
                        assertThat(it.revenueShare).isEqualTo(toPrintValue(testContext.sharePayout))
                    }
                    secondUserAddress -> {
                        assertThat(it.investments).isEqualTo(toPrintValue(testContext.reserveInvestment))
                        assertThat(it.revenueShare).isEqualTo(BigInteger.ZERO.toMwei())
                    }
                    thirdUserAddress -> {
                        assertThat(it.transactions).isEmpty()
                        assertThat(it.investments).isEqualTo(BigInteger.ZERO.toMwei())
                        assertThat(it.revenueShare).isEqualTo(BigInteger.ZERO.toMwei())
                    }
                }
            }
        }
    }

    @Test
    fun mustThrowExceptionIfNoUserWithInfoFound() {
        suppose("User service will return empty list") {
            Mockito.`when`(userService.getUsersForIssuer(IssuerRequest(issuer, chainId))).thenReturn(emptyList())
        }

        verify("Template data service can get users accounts summary") {
            val periodRequest = PeriodServiceRequest(null, null)
            val exception = assertThrows<ResourceNotFoundException> {
                templateDataService.getAllActiveUsersSummaryData(IssuerRequest(issuer, chainId), periodRequest)
            }
            assertThat(exception.errorCode).isEqualTo(ErrorCode.USER_MISSING_INFO)
        }
    }

    @Test
    fun mustThrowExceptionForEmptyListTransaction() {
        suppose("User service will return a user") {
            testContext.user = createUserResponse(userAddress)
            Mockito.`when`(userService.getUsersForIssuer(IssuerRequest(issuer, chainId)))
                .thenReturn(listOf(testContext.user))
        }
        suppose("Blockchain service will return empty list") {
            Mockito.`when`(blockchainService.getTransactions(testContext.user.address, chainId))
                .thenReturn(emptyList())
        }

        verify("Template data service can get users accounts summary") {
            val periodRequest = PeriodServiceRequest(null, null)
            val exception = assertThrows<ResourceNotFoundException> {
                templateDataService.getAllActiveUsersSummaryData(IssuerRequest(issuer, chainId), periodRequest)
            }
            assertThat(exception.errorCode).isEqualTo(ErrorCode.USER_MISSING_INFO)
        }
    }

    private fun toPrintValue(amount: Long) =
        Convert.toWei(amount.toString(), Convert.Unit.MWEI).toBigInteger().toMwei()

    private fun createEventsFlow(): List<Event> =
        listOf(
            createEvent(
                userAddress, projectWallet, TransactionType.RESERVE_INVESTMENT,
                testContext.reserveInvestment.toString()
            ),
            createEvent(
                projectWallet, userWallet, TransactionType.COMPLETED_INVESTMENT,
                testContext.completeInvestment.toString()
            ),
            createEvent(
                projectWallet, userWallet, TransactionType.CANCEL_INVESTMENT,
                testContext.cancelInvestment.toString()
            ),
        )

    private fun createTransactionFlow(): List<TransactionInfo> =
        listOf(
            createTransaction(
                userWallet, projectWallet, testContext.reserveInvestment.toString(),
                TransactionType.RESERVE_INVESTMENT
            ),
            createTransaction(
                projectWallet, userWallet, testContext.sharePayout.toString(),
                TransactionType.REVENUE_SHARE
            ),
            createTransaction(
                projectWallet, userWallet, testContext.cancelInvestment.toString(),
                TransactionType.CANCEL_INVESTMENT
            )
        )

    private class TestContext {
        lateinit var events: List<Event>
        lateinit var event: Event
        lateinit var transactionsRequest: TransactionsServiceRequest
        lateinit var transactionRequest: TransactionServiceRequest
        lateinit var user: UserResponse
        lateinit var secondUser: UserResponse
        lateinit var thirdUser: UserResponse
        val reserveInvestment = 20000L
        val cancelInvestment = 20000L
        val completeInvestment = 20000L
        val sharePayout = 1050L
    }
}
