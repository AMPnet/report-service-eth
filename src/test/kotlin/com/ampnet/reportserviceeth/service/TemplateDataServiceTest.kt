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
import com.ampnet.reportserviceeth.service.data.DATE_FORMAT
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import com.ampnet.reportserviceeth.service.data.LENGTH_OF_PERCENTAGE
import com.ampnet.reportserviceeth.service.data.TO_PERCENTAGE
import com.ampnet.reportserviceeth.service.impl.TemplateDataServiceImpl
import com.ampnet.reportserviceeth.toGwei
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TemplateDataServiceTest : JpaServiceTestBase() {

    private lateinit var testContext: TestContext

    private val templateDataService: TemplateDataService by lazy {
        TemplateDataServiceImpl(blockchainService, userService, translationService, eventService, fileService)
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
            Mockito.`when`(blockchainService.getIssuerState(chainId, issuer))
                .thenReturn(createIssuerState())
        }
        suppose("File service will return ipfs hash") {
            Mockito.`when`(fileService.getLogoHash(issuerInfo))
                .thenReturn(ipfsHash)
        }

        verify("Template data service can get user transactions") {
            val txSummary = templateDataService.getUserTransactionsData(testContext.transactionsRequest)
            assertThat(txSummary.investments)
                .isEqualTo(BigInteger.valueOf(testContext.reserveInvestment - testContext.cancelInvestment).toEther())
            assertThat(txSummary.revenueShare).isEqualTo(BigInteger.valueOf(testContext.sharePayout).toEther())

            val transactions = txSummary.transactions
            assertThat(transactions).hasSize(3)
            val investTx = transactions.first { it.type == TransactionType.RESERVE_INVESTMENT }
//            assertThat(investTx.description).isEqualTo(project.name)
//            assertThat(investTx.percentageInProject).isEqualTo(
//                getPercentageInProject(project.expectedFunding, investTx.amount)
//            )
            assertThat(investTx.txDate).isNotBlank
            assertThat(investTx.valueInDollar).isEqualTo(investTx.value.toEther())
            val cancelInvestmentTx =
                transactions.first { it.type == TransactionType.CANCEL_INVESTMENT }
//            assertThat(cancelInvestmentTx.description).isEqualTo(project.name)
//            assertThat(cancelInvestmentTx.percentageInProject).isEqualTo(
//                getPercentageInProject(project.expectedFunding, cancelInvestmentTx.amount)
//            )
            assertThat(cancelInvestmentTx.txDate).isNotBlank
            assertThat(cancelInvestmentTx.valueInDollar).isEqualTo(cancelInvestmentTx.value.toEther())
            val sharePayoutTx = transactions.first { it.type == TransactionType.COMPLETED_INVESTMENT }
//            assertThat(sharePayoutTx.description).isEqualTo(project.name)
            assertThat(sharePayoutTx.txDate).isNotBlank
            assertThat(sharePayoutTx.valueInDollar).isEqualTo(sharePayoutTx.value.toEther())

            assertThat(txSummary.logo).isEqualTo(ipfsUrl + ipfsHash)
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
                            (
                                testContext.reserveInvestment.toString().toGwei() -
                                    testContext.cancelInvestment.toString().toGwei()
                                ).toEther()
                        )
                        assertThat(it.revenueShare).isEqualTo(testContext.sharePayout.toString().toGwei().toEther())
                    }
                    secondUserAddress -> assertThat(it.investments).isEqualTo(
                        BigInteger.valueOf(testContext.reserveInvestment).toString().toGwei().toEther()
                    )
                    thirdUserAddress -> assertThat(it.transactions).isEmpty()
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

    private fun getPercentageInProject(expectedFunding: Long?, amount: Long): String? {
        return expectedFunding?.let {
            (TO_PERCENTAGE * amount / expectedFunding).toString().take(LENGTH_OF_PERCENTAGE)
        }
    }

    private fun formatToYearMonthDay(date: LocalDateTime?): String? =
        date?.format(DateTimeFormatter.ofPattern(DATE_FORMAT))

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
