package com.ampnet.reportserviceeth.controller

import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.blockchain.properties.Chain
import com.ampnet.reportserviceeth.controller.pojo.TxHistoryResponse
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.persistence.model.Event
import com.ampnet.reportserviceeth.persistence.repository.EventRepository
import com.ampnet.reportserviceeth.security.WithMockCrowdfundUser
import com.ampnet.reportserviceeth.service.data.EventServiceResponse
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import kotlin.random.Random

class TxHistoryControllerTest : ControllerTestBase() {

    @Autowired
    private lateinit var eventRepository: EventRepository

    private val path = "/tx_history"
    private val defaultIssuerAddress = "0xissuer-address"
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
            testContext.events.add(createEvent(userAddress, secondUserAddress))
            testContext.events.add(createEvent(thirdUserAddress, userAddress, type = TransactionType.TRANSFER_TOKEN))
            createEvent(userAddress, secondUserAddress, chainId = Chain.ETHEREUM_MAIN.id)
        }
        suppose("There is event for other issuer") {
            createEvent(userAddress, defaultIssuerAddress, "0xother-issuer")
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
                    timestamp = LocalDateTime.now().minusDays(3).toEpochSecond(ZoneOffset.UTC)
                )
            )
            createEvent(timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
            createEvent(timestamp = LocalDateTime.now().minusDays(10).toEpochSecond(ZoneOffset.UTC))
        }
        suppose("There is event for other issuer") {
            createEvent(userAddress, defaultIssuerAddress, "0xother-issuer")
        }

        verify("User will get transaction history only for specified period") {
            val result = mockMvc.perform(
                get("$path/$defaultChainId/$defaultIssuerAddress")
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
                get("$path/-1/$defaultIssuerAddress")
            )
                .andExpect(status().isBadRequest)
                .andReturn()
            verifyResponseErrorCode(result, ErrorCode.BLOCKCHAIN_ID)
        }
    }

    private fun createEvent(
        from: String = userAddress,
        to: String = secondUserAddress,
        issuer: String = defaultIssuerAddress,
        timestamp: Long = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
        chainId: Long = defaultChainId,
        type: TransactionType = TransactionType.COMPLETED_INVESTMENT
    ): Event {
        val event = Event(
            UUID.randomUUID(),
            chainId,
            from.lowercase(),
            to.lowercase(),
            issuer,
            UUID.randomUUID().toString(),
            type,
            Random.nextLong(),
            "asset",
            Random.nextLong(),
            UUID.randomUUID().toString(),
            timestamp,
            BigInteger.TEN,
            BigInteger.ONE,
            1,
            BigInteger.ZERO
        )
        return eventRepository.save(event)
    }

    private class TestContext {
        val events = mutableListOf<Event>()
    }
}
