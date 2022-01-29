package com.ampnet.reportserviceeth.controller

import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.blockchain.properties.Chain
import com.ampnet.reportserviceeth.controller.pojo.TxHistoryResponse
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.persistence.model.Event
import com.ampnet.reportserviceeth.security.WithMockCrowdfundUser
import com.ampnet.reportserviceeth.service.data.EventServiceResponse
import com.ampnet.reportserviceeth.util.ContractAddress
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.time.LocalDateTime

class TxHistoryControllerTest : ControllerTestBase() {

    private val path = "/tx_history"
    private val defaultIssuerAddress = ContractAddress("0xissuer-address")
    private lateinit var testContext: TestContext

    @BeforeEach
    fun init() {
        databaseCleanerService.deleteAllEvents()
        testContext = TestContext()
    }

    @Test
    @WithMockCrowdfundUser
    fun mustBeAbleToGetAllTxHistory() {
        suppose("There are stored events") {
            testContext.events.add(createEvent(userAddress, secondUserAddress, issuerAddress = defaultIssuerAddress))
            testContext.events.add(
                createEvent(
                    thirdUserAddress, userAddress, issuerAddress = defaultIssuerAddress,
                    type = TransactionType.TRANSFER_TOKEN
                )
            )
            createEvent(userAddress, secondUserAddress, chain = Chain.ETHEREUM_MAIN.id)
        }
        suppose("There is event for other issuer") {
            createEvent(userAddress, defaultIssuerAddress.asWallet(), issuerAddress = ContractAddress("0xother-issuer"))
        }

        verify("User can get his transaction history") {
            val result = mockMvc.perform(
                get("$path/$defaultChainId/$defaultIssuerAddress")
            )
                .andExpect(status().isOk)
                .andReturn()

            val response: TxHistoryResponse = objectMapper.readValue(result.response.contentAsString)
            assertThat(response.transactions).hasSize(2)
            assertThat(response.transactions).containsAll(testContext.events.map { EventServiceResponse(it) })
        }
    }

    @Test
    @WithMockCrowdfundUser
    fun mustBeAbleToGetTransactionForSpecificPeriod() {
        suppose("There are some events") {
            testContext.events.add(
                createEvent(
                    issuerAddress = defaultIssuerAddress, localDateTime = LocalDateTime.now().minusDays(3)
                )
            )
            createEvent(issuerAddress = defaultIssuerAddress, localDateTime = LocalDateTime.now())
            createEvent(issuerAddress = defaultIssuerAddress, localDateTime = LocalDateTime.now().minusDays(10))
        }
        suppose("There is event for other issuer") {
            createEvent(userAddress, defaultIssuerAddress.asWallet(), issuerAddress = ContractAddress("0xother-issuer"))
        }

        verify("User will get transaction history only for specified period") {
            val result = mockMvc.perform(
                get("$path/${defaultChainId.value}/${defaultIssuerAddress.value}")
                    .param("from", LocalDate.now().minusDays(5).toString())
                    .param("to", LocalDate.now().minusDays(1).toString())
            )
                .andExpect(status().isOk)
                .andReturn()

            val response: TxHistoryResponse = objectMapper.readValue(result.response.contentAsString)
            assertThat(response.transactions).hasSize(1)
            assertThat(response.transactions).containsAll(testContext.events.map { EventServiceResponse(it) })
        }
    }

    @Test
    @WithMockCrowdfundUser
    fun mustReturnErrorForInvalidChainId() {
        verify("Controller will return bad request") {
            val result = mockMvc.perform(
                get("$path/-1/${defaultIssuerAddress.value}")
            )
                .andExpect(status().isBadRequest)
                .andReturn()
            verifyResponseErrorCode(result, ErrorCode.BLOCKCHAIN_ID)
        }
    }

    private class TestContext {
        val events = mutableListOf<Event>()
    }
}
