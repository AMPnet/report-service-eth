package com.ampnet.reportserviceeth.service

import com.ampnet.identityservice.proto.UserResponse
import com.ampnet.reportserviceeth.blockchain.TransactionInfo
import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionServiceRequest
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InvalidRequestException
import com.ampnet.reportserviceeth.exception.ResourceNotFoundException
import com.ampnet.reportserviceeth.service.data.DATE_FORMAT
import com.ampnet.reportserviceeth.service.data.DEFAULT_LOGO
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TemplateDataServiceTest : JpaServiceTestBase() {

    private lateinit var testContext: TestContext

    private val templateDataService: TemplateDataService by lazy {
        TemplateDataServiceImpl(blockchainService, blockchainEventService, userService, translationService)
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
        suppose("Blockchain service will return transactions for wallet") {
            testContext.transactions = listOf(
                createTransaction(
                    userAddress, projectWallet, testContext.reserveInvestment.toString(),
                    TransactionType.RESERVE_INVESTMENT
                ),
                createTransaction(
                    projectWallet, userAddress, testContext.cancelInvestment.toString(),
                    TransactionType.CANCEL_INVESTMENT
                ),
                createTransaction(
                    projectWallet, userAddress, testContext.completeInvestment.toString(),
                    TransactionType.COMPLETED_INVESTMENT
                ),
            )
            Mockito.`when`(blockchainService.getTransactions(testContext.user.address, chainId))
                .thenReturn(testContext.transactions)
        }

        verify("Template data service can get user transactions") {
            val periodRequest = PeriodServiceRequest(null, null)
            val txSummary = templateDataService.getUserTransactionsData(userAddress, chainId, periodRequest)
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
        }
    }

    @Test
    fun mustNotIncludeTransactionsOutsideOfSelectedPeriod() {
        suppose("gRPC user service will return required data") {
            testContext.user = createUserResponse(userAddress)
            Mockito.`when`(userService.getUser(userAddress))
                .thenReturn(testContext.user)
        }
        suppose("Blockchain service will return transactions for wallet") {
            testContext.transactions = listOf(
                createTransaction(
                    userAddress, projectWallet, testContext.reserveInvestment.toString(),
                    TransactionType.RESERVE_INVESTMENT,
                    LocalDateTime.of(2020, 9, 1, 1, 0, 0, 0)
                ),
                createTransaction(
                    userAddress, projectWallet, testContext.reserveInvestment.toString(),
                    TransactionType.RESERVE_INVESTMENT,
                    LocalDateTime.of(2020, 8, 1, 1, 0, 0, 0)
                ),
                createTransaction(
                    userAddress, projectWallet, testContext.reserveInvestment.toString(),
                    TransactionType.RESERVE_INVESTMENT,
                    LocalDateTime.of(2020, 7, 1, 1, 0, 0, 0)
                ),
                createTransaction(
                    userAddress, projectWallet, testContext.reserveInvestment.toString(),
                    TransactionType.RESERVE_INVESTMENT,
                    LocalDateTime.of(2020, 6, 1, 1, 0, 0, 0)
                )
            )
            Mockito.`when`(blockchainService.getTransactions(testContext.user.address, chainId))
                .thenReturn(testContext.transactions)
        }

        verify("Template data service can get user transactions in selected period") {
            val periodRequest = PeriodServiceRequest(
                LocalDate.of(2020, 7, 1),
                LocalDate.of(2020, 9, 1)
            )
            val txSummary = templateDataService.getUserTransactionsData(userAddress, chainId, periodRequest)
            assertThat(txSummary.transactions).hasSize(3)
            assertThat(txSummary.dateOfFinish).isEqualTo(formatToYearMonthDay(periodRequest.to))
        }
    }

    @Test
    fun mustGenerateCorrectSingleTransactionSummary() {
        suppose("Blockchain service will return transaction info for txHash, fromTxHash and toTxHash") {
            testContext.transaction = createTransaction(
                userAddress, projectWallet, testContext.reserveInvestment.toString(),
                TransactionType.RESERVE_INVESTMENT, txHash = txHash
            )
            Mockito.`when`(blockchainEventService.getTransactionInfo(txHash, chainId))
                .thenReturn(testContext.transaction)
        }
        suppose("User service will return userWithInfo") {
            testContext.user = createUserResponse(userAddress)
            Mockito.`when`(userService.getUser(userAddress))
                .thenReturn(testContext.user)
        }

        verify("Template data service can get user transaction") {
            val transaction = testContext.transaction
            val transactionServiceRequest = TransactionServiceRequest(userAddress, txHash, chainId)
            val singleTxSummary = templateDataService.getUserTransactionData(transactionServiceRequest)
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
        }
    }

    @Test
    fun mustThrowExceptionIfTransactionDoesNotBelongToUser() {
        suppose("Blockchain service will return transaction for tx hash") {
            testContext.transaction = createTransaction(
                secondUserAddress,
                projectWallet,
                testContext.reserveInvestment.toString(),
                TransactionType.RESERVE_INVESTMENT
            )
            Mockito.`when`(blockchainEventService.getTransactionInfo(txHash, chainId))
                .thenReturn(testContext.transaction)
        }

        verify("Template data service will throw exception if tx doesn't belong to user wallet") {
            val transactionServiceRequest = TransactionServiceRequest(userAddress, txHash, chainId)
            val exception = assertThrows<InvalidRequestException> {
                templateDataService.getUserTransactionData(transactionServiceRequest)
            }
            assertThat(exception.errorCode).isEqualTo(ErrorCode.INT_REQUEST)
        }
    }

    @Test
    fun mustGenerateSingleReportInEnglishOnInvalidLanguage() {
        suppose("Blockchain service will return transaction info for txHash") {
            testContext.transaction = createTransaction(
                userAddress, projectWallet, testContext.reserveInvestment.toString(), TransactionType.RESERVE_INVESTMENT
            )
            Mockito.`when`(blockchainEventService.getTransactionInfo(txHash, chainId))
                .thenReturn(testContext.transaction)
        }
        suppose("User service will return userWithInfo with invalid language") {
            testContext.user = createUserResponse(userAddress, "invalid_language")
            Mockito.`when`(userService.getUser(userAddress))
                .thenReturn(testContext.user)
        }

        verify("Template data service can get user transaction") {
            val transaction = testContext.transaction
            val transactionServiceRequest = TransactionServiceRequest(userAddress, transaction.txHash, chainId)
            val singleTxSummary = templateDataService.getUserTransactionData(transactionServiceRequest)
            assertThat(singleTxSummary.translations.transactions).isEqualTo("Transactions")
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
            testContext.transactions = createUserTransactionFlow()
            Mockito.`when`(blockchainService.getTransactions(testContext.user.address, chainId))
                .thenReturn(testContext.transactions)
        }

        verify("Template data service can get user transactions") {
            val periodRequest = PeriodServiceRequest(null, null)
            val txSummary = templateDataService.getUserTransactionsData(userAddress, chainId, periodRequest)
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
                .thenReturn(createUserTransactionFlow())
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
            val usersAccountsSummary = templateDataService.getAllActiveUsersSummaryData(IssuerRequest(issuer, chainId), periodRequest)
            val transactionsSummaryList = usersAccountsSummary.summaries
            val logo = usersAccountsSummary.logo
            assertThat(transactionsSummaryList).hasSize(3)
            assertThat(logo).isEqualTo(DEFAULT_LOGO)
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

    private fun createUserTransactionFlow(): List<TransactionInfo> =
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
            ),
        )

    private fun getPercentageInProject(expectedFunding: Long?, amount: Long): String? {
        return expectedFunding?.let {
            (TO_PERCENTAGE * amount / expectedFunding).toString().take(LENGTH_OF_PERCENTAGE)
        }
    }

    private fun formatToYearMonthDay(date: LocalDateTime?): String? =
        date?.format(DateTimeFormatter.ofPattern(DATE_FORMAT))

    private class TestContext {
        lateinit var transactions: List<TransactionInfo>
        lateinit var transaction: TransactionInfo
        lateinit var user: UserResponse
        lateinit var secondUser: UserResponse
        lateinit var thirdUser: UserResponse
        val reserveInvestment = 20000L
        val cancelInvestment = 20000L
        val completeInvestment = 20000L
        val sharePayout = 1050L
    }
}
