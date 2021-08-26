package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.TestBase
import com.ampnet.reportserviceeth.blockchain.BlockchainEventService
import com.ampnet.reportserviceeth.blockchain.BlockchainService
import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.blockchain.properties.Chain
import com.ampnet.reportserviceeth.config.ApplicationProperties
import com.ampnet.reportserviceeth.config.DatabaseCleanerService
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.persistence.model.Event
import com.ampnet.reportserviceeth.persistence.model.Task
import com.ampnet.reportserviceeth.persistence.repository.EventRepository
import com.ampnet.reportserviceeth.persistence.repository.TaskRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.math.BigInteger
import java.time.Instant
import java.util.UUID

@SpringBootTest
class BlockchainQueueServiceTest : TestBase() {

    private val chainId = Chain.MATIC_TESTNET_MUMBAI.id
    private val startBlockNumber: Long = 100
    private val lastBlockNumber: Long = 200
    private lateinit var testContext: TestContext

    @Autowired
    private lateinit var databaseCleanerService: DatabaseCleanerService

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var applicationProperties: ApplicationProperties

    @MockBean
    private lateinit var blockchainEventService: BlockchainEventService

    @MockBean
    private lateinit var blockchainService: BlockchainService

    @BeforeEach
    fun init() {
        testContext = TestContext()
        databaseCleanerService.deleteAllTasks()
        databaseCleanerService.deleteAllEvents()
    }

    @AfterEach
    fun after() {
        databaseCleanerService.deleteAllTasks()
        databaseCleanerService.deleteAllEvents()
    }

    @Test
    fun mustSaveEventsOnExistingTask() {
        suppose("There is an existing task") {
            testContext.task = taskRepository.save(Task(chainId, startBlockNumber))
        }
        suppose("Blockchain service will return latest block number") {
            given(blockchainService.getBlockNumber(chainId)).willReturn(BigInteger.valueOf(lastBlockNumber))
        }
        suppose("Blockchain service will return two events") {
            testContext.investEvent = createEvent(TransactionType.RESERVE_INVESTMENT, "blockHash1")
            testContext.cancelInvestmentEvent = createEvent(TransactionType.CANCEL_INVESTMENT, "blockHash2")
            given(
                blockchainEventService.getAllEvents(
                    startBlockNumber + 1,
                    lastBlockNumber - applicationProperties.chainMumbai.numOfConfirmations, chainId
                )
            ).willReturn(listOf(testContext.investEvent, testContext.cancelInvestmentEvent))
        }

        verify("Service will save events and create a second task") {
            waitUntilTasksAreProcessed()
            val tasks = taskRepository.findAll()
            val events = eventRepository.findAll()
            val lastTask = taskRepository.findFirstByOrderByBlockNumberDesc() ?: fail("Cannot find task")
            assertThat(tasks).hasSize(2)
            assertThat(lastTask.chainId).isEqualTo(chainId)
            assertThat(lastTask.blockNumber).isEqualTo(
                lastBlockNumber - applicationProperties.chainMumbai.numOfConfirmations
            )

            assertThat(events).hasSize(2)
        }
    }

    @Test
    fun mustNotSaveTaskOrEventOnExceptionThrown() {
        suppose("There is an existing task") {
            testContext.task = taskRepository.save(Task(chainId, startBlockNumber))
        }
        suppose("Blockchain service will return latest block number") {
            given(blockchainService.getBlockNumber(chainId))
                .willReturn(BigInteger.valueOf(lastBlockNumber))
        }
        suppose("Blockchain service will return two events") {
            given(
                blockchainEventService.getAllEvents(
                    startBlockNumber + 1,
                    lastBlockNumber - applicationProperties.chainMumbai.numOfConfirmations, chainId
                )
            ).willAnswer { throw InternalException(ErrorCode.INT_JSON_RPC_BLOCKCHAIN, "blockchain exception") }
        }

        verify("Service will not save any events or additional task") {
            waitUntilTasksAreProcessed()
            val tasks = taskRepository.findAll()
            val task = tasks.first()
            val events = eventRepository.findAll()
            assertThat(task.chainId).isEqualTo(chainId)
            assertThat(task.blockNumber).isEqualTo(startBlockNumber)

            assertThat(events).isEmpty()
        }
    }

    private fun waitUntilTasksAreProcessed() {
        Thread.sleep(applicationProperties.queue.initialDelay * 2)
    }

    private fun createEvent(transactionType: TransactionType, blockHash: String) =
        Event(
            UUID.randomUUID(), chainId, "fromAddress", "toAddress", "contractAddress",
            "txHash", transactionType, 10, "assetName", 100, blockHash,
            Instant.now().toEpochMilli(), null, null, null, null
        )

    private class TestContext {
        lateinit var task: Task
        lateinit var investEvent: Event
        lateinit var cancelInvestmentEvent: Event
    }
}
