package com.ampnet.reportserviceeth.controller

import com.ampnet.identityservice.proto.UserResponse
import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.persistence.model.Event
import com.ampnet.reportserviceeth.security.WithMockCrowdfundUser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

class ReportingControllerTest : ControllerTestBase() {

    private val reportPath = "/report/$defaultChainId/$issuer/user/"
    private val transaction = "transaction"
    private val transactions = "transactions"

    private val userTransactionsPath = reportPath + transactions
    private val userTransactionPath = reportPath + transaction

    private lateinit var testContext: TestContext

    @BeforeEach
    fun init() {
        databaseCleanerService.deleteAllEvents()
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
        suppose("There are events for user wallet") {
            testContext.events = createEventsResponse()
        }
        suppose("Blockchain service will return issuer state") {
            Mockito.`when`(blockchainService.getIssuerState(defaultChainId, issuer))
                .thenReturn(createIssuerState())
        }
        suppose("File service will return ipfs hash") {
            Mockito.`when`(ipfsService.getLogoHash(issuerInfo))
                .thenReturn(ipfsHash)
        }

        verify("User can get pdf with all transactions") {
            val from = LocalDate.of(2019, 10, 10)
            val to = LocalDate.now().plusDays(1)
            val result = mockMvc.perform(
                get(userTransactionsPath)
                    .param("from", from?.toString())
                    .param("to", to?.toString())
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
        suppose("There is an event") {
            testContext.event = createEvent(
                userAddress, projectWallet, TransactionType.RESERVE_INVESTMENT,
                "700", txHash = txHash
            )
        }
        suppose("Blockchain service will return issuer state") {
            Mockito.`when`(blockchainService.getIssuerState(defaultChainId, issuer))
                .thenReturn(createIssuerState())
        }
        suppose("File service will return ipfs hash") {
            Mockito.`when`(ipfsService.getLogoHash(issuerInfo))
                .thenReturn(ipfsHash)
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
        lateinit var events: List<Event>
        lateinit var event: Event
        lateinit var user: UserResponse
    }
}
