package com.ampnet.reportserviceeth.repository

import com.ampnet.reportserviceeth.TestBase
import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.blockchain.properties.Chain
import com.ampnet.reportserviceeth.config.DatabaseCleanerService
import com.ampnet.reportserviceeth.persistence.model.Event
import com.ampnet.reportserviceeth.persistence.repository.EventRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.UUID

@ExtendWith(SpringExtension::class)
@DataJpaTest
@Import(DatabaseCleanerService::class)
class RepositoryTest : TestBase() {

    private lateinit var testContext: TestContext

    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var databaseCleanerService: DatabaseCleanerService

    private val txHash = "0xab059a62e22e230fe0f56d8555340a29b2e9532360368f810595453f6fdd213b"
    private val logIndex = 23L
    private val blockHash = "0x8243343df08b9751f5ca0c5f8c9c0460d8a9b6351066fae0acbd4d3e776de8bb"
    private val chainId = Chain.MATIC_TESTNET_MUMBAI.id
    private val userAddress = "0x8f52B0cC50967fc59C6289f8FDB3E356EdeEBD23"
    private val secondUserAddress = "0xd43e088622404A5A21267033EC200383d39C22ca"
    private val thirdUserAddress = "0xd43e088622404A5A21267033EC200383d39C22cc"
    private val fourthUserAddress = "0xd43e088622404A5A21267033EC200383d39C22cd"
    private val issuer = "0x5013F6ce0f9Beb07Be528E408352D03f3FCa1857"
    private val projectWallet: String = "0xFeC646017105fA2A4FFDc773e9c539Eda5af724a"

    @BeforeEach
    fun init() {
        testContext = TestContext()
        databaseCleanerService.deleteAllEvents()
        databaseCleanerService.deleteAllTasks()
    }

    @Test
    fun mustNotBeAbleToSaveDuplicateEvent() {
        eventRepository.saveAndFlush(
            createEvent(
                txHash = txHash, logIndex = logIndex, blockHash = blockHash, save = false
            )
        )
        assertThrows<RuntimeException> {
            eventRepository.saveAndFlush(
                createEvent(txHash = txHash, logIndex = logIndex, blockHash = blockHash, save = false)
            )
        }
    }

    @Test
    fun mustReturnEventsForIssuerChainIdAndAddressInSelectedPeriod() {
        suppose("There are events for issuer") {
            testContext.firstEvent = createEvent()
            testContext.secondEvent = createEvent(type = TransactionType.CANCEL_INVESTMENT)
        }
        suppose("There are events from another issuer") {
            createEvent(issuerAddress = "Ox-another-issuer")
            createEvent(issuerAddress = "Ox-some-other-issuer")
        }
        suppose("There is event outside of selected period") {
            createEvent(localDateTime = LocalDateTime.now().minusHours(50))
        }

        verify("Repository returns correct events") {
            val events = eventRepository.findForAddressInPeriod(
                userAddress, chainId.value, issuer, LocalDateTime.now().minusDays(1).toEpochSecond(ZoneOffset.UTC), null
            )
            assertThat(events).hasSize(2)
        }
    }

    @Test
    fun mustReturnEventForTxHashIssuerChainIdAndAddress() {
        suppose("There are events for issuer") {
            testContext.firstEvent = createEvent()
            testContext.secondEvent = createEvent(type = TransactionType.CANCEL_INVESTMENT, txHash = txHash)
        }
        suppose("There are events from another issuer") {
            createEvent(issuerAddress = "Ox-another-issuer", txHash = txHash, blockHash = "535")
            createEvent(issuerAddress = "Ox-some-other-issuer", txHash = txHash, blockHash = "536")
        }

        verify("Repository returns correct event") {
            val event = eventRepository.findForTxHash(txHash, issuer, userAddress, chainId.value)
                ?: fail("event missing")
            assertThat(event.type).isEqualTo(TransactionType.CANCEL_INVESTMENT)
        }
    }

    @Test
    fun mustReturnLatestSuccessfulInvestmentEventsByIssuerAndCampaign() {
        val firstUserEvents = TestContext()
        val secondUserEvents = TestContext()
        val thirdUserEvents = TestContext()
        val olderEventTime = LocalDateTime.now()
        val newerEventTime = LocalDateTime.now().plusDays(1)

        suppose("First user has cancelled investment") {
            firstUserEvents.firstEvent = createEvent(
                type = TransactionType.RESERVE_INVESTMENT,
                localDateTime = olderEventTime
            )
            firstUserEvents.secondEvent = createEvent(
                type = TransactionType.CANCEL_INVESTMENT,
                localDateTime = newerEventTime
            )
        }

        suppose("Second user has not cancelled investment") {
            secondUserEvents.firstEvent = createEvent(
                from = secondUserAddress,
                type = TransactionType.RESERVE_INVESTMENT,
                localDateTime = olderEventTime
            )
            secondUserEvents.secondEvent = createEvent(
                from = secondUserAddress,
                type = TransactionType.RESERVE_INVESTMENT,
                localDateTime = newerEventTime
            )
        }

        suppose("Third user has canceled investment and then invested again") {
            thirdUserEvents.firstEvent = createEvent(
                from = thirdUserAddress,
                type = TransactionType.CANCEL_INVESTMENT,
                localDateTime = olderEventTime
            )
            thirdUserEvents.secondEvent = createEvent(
                from = thirdUserAddress,
                type = TransactionType.COMPLETED_INVESTMENT,
                localDateTime = newerEventTime
            )
        }

        suppose("There are events from another issuer") {
            createEvent(
                from = fourthUserAddress,
                issuerAddress = "Ox-another-issuer",
                txHash = txHash,
                blockHash = "535"
            )
            createEvent(
                from = fourthUserAddress,
                issuerAddress = "Ox-some-other-issuer",
                txHash = txHash,
                blockHash = "536"
            )
        }

        verify("Repository returns correct latest successful investment events") {
            val events = eventRepository.findLatestSuccessfulInvestmentEventsByIssuerAndCampaign(issuer, projectWallet)
            assertThat(events).hasSize(2)

            assertThat(events.map { it.uuid }).containsExactlyInAnyOrder(
                secondUserEvents.secondEvent.uuid,
                thirdUserEvents.secondEvent.uuid
            )
            assertThat(events.map { it.issuer }.toSet()).containsExactlyInAnyOrder(issuer)
            assertThat(events.map { it.contract }.toSet()).containsExactlyInAnyOrder(projectWallet)
            assertThat(events.map { it.fromAddress }).containsExactlyInAnyOrder(
                secondUserAddress,
                thirdUserAddress
            )
            assertThat(events.map { it.type }).containsExactlyInAnyOrder(
                TransactionType.COMPLETED_INVESTMENT,
                TransactionType.RESERVE_INVESTMENT
            )
        }
    }

    private fun createEvent(
        from: String = userAddress,
        to: String = secondUserAddress,
        contractAddress: String = projectWallet,
        txHash: String = UUID.randomUUID().toString(),
        issuerAddress: String = issuer,
        type: TransactionType = TransactionType.COMPLETED_INVESTMENT,
        chain: Long = chainId.value,
        logIndex: Long = 134L,
        blockHash: String = "blockHash",
        localDateTime: LocalDateTime = LocalDateTime.now(),
        save: Boolean = true
    ): Event {
        val event = Event(
            UUID.randomUUID(), chain, from, to,
            contractAddress, issuerAddress, txHash, type,
            logIndex, "asset_name", "symbol", 500045L, blockHash,
            localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000,
            BigInteger("500"), 0, BigInteger("500"),
            50L, BigInteger("500"), 6
        )
        return if (save) eventRepository.save(event)
        else event
    }

    private class TestContext {
        lateinit var firstEvent: Event
        lateinit var secondEvent: Event
    }
}
