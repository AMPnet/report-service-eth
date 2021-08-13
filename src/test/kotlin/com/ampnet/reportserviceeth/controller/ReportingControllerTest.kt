package com.ampnet.reportserviceeth.controller

import com.ampnet.identityservice.proto.UserResponse
import com.ampnet.reportserviceeth.blockchain.TransactionInfo
import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.security.WithMockCrowdfundUser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

class ReportingControllerTest : ControllerTestBase() {

    private val reportPath = "/report/$chainId/user/"
    private val transaction = "transaction"
    private val transactions = "transactions"

    private val userTransactionsPath = reportPath + transactions
    private val userTransactionPath = reportPath + transaction

    private lateinit var testContext: TestContext

    @BeforeEach
    fun init() {
        testContext = TestContext()
    }

    @Test
    @WithMockCrowdfundUser
    fun mustBeAbleToGeneratePdfForAllUserTransactions() {
        suppose("User service will return the user") {
            testContext.user = createUserResponse(userAddress)
            Mockito.`when`(userService.getUser(userAddress))
                .thenReturn(testContext.user)
        }
        suppose("Blockchain service will return transactions for wallet") {
            testContext.transactions = createTransactionsResponse()
            Mockito.`when`(blockchainService.getTransactions(testContext.user.address, chainId))
                .thenReturn(testContext.transactions)
        }

        verify("User can get pdf with all transactions") {
            val result = mockMvc.perform(
                get(userTransactionsPath)
                    .param("from", "2019-10-10")
                    .param("to", LocalDate.now().plusDays(1).toString())
            )
                .andExpect(status().isOk)
                .andReturn()

            val pdfContent = result.response.contentAsByteArray
            verifyPdfFormat(pdfContent)
            // File(getDownloadDirectory("transactions.pdf")).writeBytes(pdfContent)
        }
    }

    @Test
    @WithMockCrowdfundUser
    fun mustBeAbleToGeneratePdfForUserTransaction() {
        suppose("User service will return the user") {
            testContext.user = createUserResponse(userAddress)
            Mockito.`when`(userService.getUser(userAddress))
                .thenReturn(testContext.user)
        }
        suppose("Blockchain service will return transaction info for txHash, fromTxHash and toTxHash") {
            testContext.transaction = createTransaction(
                TransactionType.RESERVE_INVESTMENT,
                "0x316E89e5455DaD761f289Ead1F612Ab0b3bF32Bf",
                userAddress,
                "700"
            )
            given(blockchainService.getTransactionInfo(txHash, chainId)).willReturn(testContext.transaction)
        }

        verify("User can get pdf with single transaction") {
            val result = mockMvc.perform(
                get(userTransactionPath)
                    .param("txHash", txHash)
            )
                .andExpect(status().isOk)
                .andReturn()

            val pdfContent = result.response.contentAsByteArray
            verifyPdfFormat(pdfContent)
            // File(getDownloadDirectory("transaction.pdf")).writeBytes(pdfContent)
        }
    }

    private class TestContext {
        lateinit var transactions: List<TransactionInfo>
        lateinit var transaction: TransactionInfo
        lateinit var user: UserResponse
    }
}
