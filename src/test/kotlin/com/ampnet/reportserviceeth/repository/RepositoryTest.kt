package com.ampnet.reportserviceeth.repository

import com.ampnet.reportserviceeth.TestBase
import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.blockchain.properties.Chain
import com.ampnet.reportserviceeth.config.DatabaseCleanerService
import com.ampnet.reportserviceeth.persistence.model.Event
import com.ampnet.reportserviceeth.persistence.model.Task
import com.ampnet.reportserviceeth.persistence.repository.EventRepository
import com.ampnet.reportserviceeth.persistence.repository.TaskRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.fail

@ExtendWith(SpringExtension::class)
@DataJpaTest
@Import(DatabaseCleanerService::class)
class RepositoryTest : TestBase() {

    private lateinit var testContext: TestContext

    @Autowired
    private lateinit var taskRepository: TaskRepository

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
    private val issuer = "0x5013F6ce0f9Beb07Be528E408352D03f3FCa1857"

    @BeforeEach
    fun init() {
        testContext = TestContext()
        databaseCleanerService.deleteAllEvents()
        databaseCleanerService.deleteAllTasks()
    }

    @Test
    fun mustNotBeAbleToSaveDuplicateEvent() {
        eventRepository.saveAndFlush(createEvent(
            txHash = txHash, logIndex = logIndex, blockHash = blockHash, save = false)
        )
        assertThrows<RuntimeException> { eventRepository.saveAndFlush(
            createEvent(txHash = txHash, logIndex = logIndex, blockHash = blockHash, save = false)
        ) }
    }

    @Test
    fun mustReturnEventsForIssuerChainIdAndAddressInSelectedPeriod() {
        suppose("There are events for issuer") {
            testContext.firstEvent = createEvent()
            testContext.secondEvent = createEvent(type = TransactionType.CANCEL_INVESTMENT, txHash = "txHash2")
        }
        suppose("There are events from another issuer") {
            createEvent(contractAddress = "Ox-another-issuer", txHash = "txHash3")
            createEvent(contractAddress = "Ox-some-other-issuer", txHash = "txHash4")
        }
        suppose("There is event outside of selected period") {
            createEvent(
                timestamp = LocalDateTime.now().minusHours(50).toEpochSecond(ZoneOffset.UTC), txHash = "txHash5"
            )

        }

        verify("Repository returns correct events") {
            val events = eventRepository.findForAddressAndIssuerInPeriod(
                issuer, userAddress, chainId, LocalDateTime.now().minusDays(1).toEpochSecond(ZoneOffset.UTC), null
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
            createEvent(contractAddress = "Ox-another-issuer", txHash = txHash, blockHash = "535")
            createEvent(contractAddress = "Ox-some-other-issuer", txHash = txHash,  blockHash = "536")
        }

        verify("Repository returns correct event") {
            val event = eventRepository.findForTxHash(txHash, issuer, userAddress, chainId)
                ?: fail("Transaction missing")
            assertThat(event.type).isEqualTo(TransactionType.CANCEL_INVESTMENT)
        }
    }

    @Test
    fun mustBeAbleToSaveTask() {
        taskRepository.save(createTask())
    }

    private fun createEvent(
        chain: Long = chainId, from: String = userAddress, to: String = secondUserAddress,
        contractAddress: String = issuer, txHash: String = "txHash",
        type: TransactionType = TransactionType.COMPLETED_INVESTMENT, logIndex: Long = 134L,
        blockHash: String = "blockHash", timestamp: Long = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
        save: Boolean = true
    ): Event {
        val event = Event(
            UUID.randomUUID(), chain, from, to,
            contractAddress, txHash, type,
            logIndex, "asset_name", 500045L, blockHash,
            timestamp, BigInteger("500"), BigInteger("500"), 50L, BigInteger("500")
        )
        return if (save) eventRepository.save(event)
        else event
    }


    private fun createTask() = Task(UUID.randomUUID(), chainId, 5075L, 1628065107449L)

    private class TestContext {
        lateinit var firstEvent: Event
        lateinit var secondEvent: Event
    }
}
