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
import com.ampnet.reportserviceeth.util.BlockNumber
import com.ampnet.reportserviceeth.util.ChainId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito
import org.mockito.kotlin.given
import org.mockito.kotlin.timeout
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.math.BigInteger
import java.time.Instant
import java.util.UUID

@SpringBootTest
class EventQueueServiceTest : TestBase() {

    private val defaultChainId = Chain.MATIC_TESTNET_MUMBAI.id
    private val maticChainId = Chain.MATIC_MAIN.id
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
        Mockito.clearInvocations(blockchainEventService)
        given(blockchainService.getBlockNumber(ChainId(anyLong()))).willReturn(BlockNumber(-1L))
        given(
            blockchainEventService.getAllEvents(
                anyValueClass(BlockNumber(0L)),
                anyValueClass(BlockNumber(0L)),
                ChainId(anyLong())
            )
        ).willReturn(emptyList())
    }

    @AfterEach
    fun after() {
        databaseCleanerService.deleteAllTasks()
        databaseCleanerService.deleteAllEvents()
    }

    @Test
    fun mustSaveEventsOnExistingTask() {
        suppose("There is an existing task") {
            testContext.task = taskRepository.save(Task(defaultChainId.value, startBlockNumber))
        }
        suppose("Blockchain service will return two events") {
            given(
                blockchainEventService.getAllEvents(
                    BlockNumber(startBlockNumber + 1L).mockito(),
                    BlockNumber(
                        lastBlockNumber - applicationProperties.chainMumbai.numOfConfirmations.toLong()
                    ).mockito(),
                    defaultChainId.mockito()
                )
            ).willReturn(
                listOf(
                    createEvent(TransactionType.RESERVE_INVESTMENT, "blockHash1"),
                    createEvent(TransactionType.CANCEL_INVESTMENT, "blockHash2")
                )
            )
        }
        suppose("Blockchain service will return latest block number") {
            given(blockchainService.getBlockNumber(defaultChainId.mockito()))
                .willReturn(BlockNumber(lastBlockNumber))
        }

        verify("Service will save events and update the task") {
            waitUntilTasksAreProcessed()
            val tasks = taskRepository.findAll()
            val onlyTask = tasks.first()
            assertThat(tasks).hasSize(1)
            assertThat(onlyTask.chainId).isEqualTo(defaultChainId.value)
            assertThat(onlyTask.blockNumber).isEqualTo(
                lastBlockNumber - applicationProperties.chainMumbai.numOfConfirmations.toLong()
            )
            assertThat(onlyTask.timestamp).isGreaterThanOrEqualTo(testContext.task.timestamp)

            val events = eventRepository.findAll()
            assertThat(events).hasSize(2)
        }
    }

    @Test
    fun mustSaveEventsFromMultipleChains() {
        suppose("There is an existing task for two chains") {
            testContext.task = taskRepository.save(Task(defaultChainId.value, startBlockNumber))
            testContext.anotherTask = taskRepository.save(Task(maticChainId.value, startBlockNumber))
        }
        suppose("Blockchain service will return two events for mumbai testnet") {
            given(
                blockchainEventService.getAllEvents(
                    BlockNumber(startBlockNumber + 1L).mockito(),
                    BlockNumber(
                        lastBlockNumber - applicationProperties.chainMumbai.numOfConfirmations.toLong()
                    ).mockito(),
                    defaultChainId.mockito()
                )
            ).willReturn(
                listOf(
                    createEvent(TransactionType.RESERVE_INVESTMENT, "blockHash1"),
                    createEvent(TransactionType.CANCEL_INVESTMENT, "blockHash2")
                )
            )
        }
        suppose("Blockchain service will return two events for matic mainnet") {
            given(
                blockchainEventService.getAllEvents(
                    BlockNumber(startBlockNumber + 1L).mockito(),
                    BlockNumber(
                        lastBlockNumber - applicationProperties.chainMatic.numOfConfirmations.toLong()
                    ).mockito(),
                    maticChainId.mockito()
                )
            ).willReturn(
                listOf(
                    createEvent(TransactionType.RESERVE_INVESTMENT, "blockHash3", maticChainId),
                    createEvent(TransactionType.CANCEL_INVESTMENT, "blockHash4", maticChainId)
                )
            )
        }
        suppose("Blockchain service will return latest block number for both chains") {
            given(blockchainService.getBlockNumber(anyValueClass(ChainId(0L)))).willReturn(BlockNumber(lastBlockNumber))
        }

        verify("Service will save events and update tasks") {
            waitUntilTasksAreProcessed()
            waitUntilTasksAreProcessed(maticChainId)

            val tasks = taskRepository.findAll()
            assertThat(tasks).hasSize(2)
            val mumbaiTask = taskRepository.findByChainId(defaultChainId.value) ?: fail("Cannot find the task")
            assertThat(mumbaiTask.chainId).isEqualTo(defaultChainId.value)
            assertThat(mumbaiTask.blockNumber).isEqualTo(
                lastBlockNumber - applicationProperties.chainMumbai.numOfConfirmations.toLong()
            )
            assertThat(mumbaiTask.timestamp).isGreaterThanOrEqualTo(testContext.task.timestamp)

            val maticTask = taskRepository.findByChainId(maticChainId.value)
                ?: fail("Cannot find the task")
            assertThat(maticTask.chainId).isEqualTo(maticChainId.value)
            assertThat(maticTask.blockNumber).isEqualTo(
                lastBlockNumber - applicationProperties.chainMatic.numOfConfirmations.toLong()
            )
            assertThat(maticTask.timestamp).isGreaterThanOrEqualTo(testContext.anotherTask.timestamp)

            val events = eventRepository.findAll()
            assertThat(events).hasSize(4)
            @Suppress("DEPRECATION") val mumbaiEvents = eventRepository.findByChainId(defaultChainId.value)
            assertThat(mumbaiEvents).hasSize(2)
            @Suppress("DEPRECATION") val maticEvents = eventRepository.findByChainId(maticChainId.value)
            assertThat(maticEvents).hasSize(2)
        }
    }

    @Test
    fun mustNotSaveTaskOrEventOnExceptionThrown() {
        suppose("There is an existing task") {
            testContext.task = taskRepository.save(Task(defaultChainId.value, startBlockNumber))
        }
        suppose("Blockchain service will return latest block number") {
            given(blockchainService.getBlockNumber(defaultChainId.mockito()))
                .willReturn(BlockNumber(lastBlockNumber))
        }
        suppose("Blockchain service will return two events") {
            given(
                blockchainEventService.getAllEvents(
                    BlockNumber(startBlockNumber + 1L).mockito(),
                    BlockNumber(
                        lastBlockNumber - applicationProperties.chainMumbai.numOfConfirmations.toLong()
                    ).mockito(),
                    defaultChainId.mockito()
                )
            ).willAnswer { throw InternalException(ErrorCode.INT_JSON_RPC_BLOCKCHAIN, "blockchain exception") }
        }

        verify("Service will not save any events or update task") {
            waitUntilTasksAreProcessed()
            val tasks = taskRepository.findAll()
            val task = tasks.first()
            assertThat(task.chainId).isEqualTo(defaultChainId.value)
            assertThat(task.blockNumber).isEqualTo(startBlockNumber)

            val events = eventRepository.findAll()
            assertThat(events).isEmpty()
        }
    }

    private fun waitUntilTasksAreProcessed(chainId: ChainId = defaultChainId) {
        Mockito.verify(blockchainEventService, timeout(10000).atLeastOnce())
            .getAllEvents(
                BlockNumber(startBlockNumber + 1).mockito(),
                BlockNumber(lastBlockNumber - applicationProperties.chainMumbai.numOfConfirmations.toLong()).mockito(),
                chainId.mockito()
            )
        Thread.sleep(applicationProperties.queue.polling / 2)
    }

    private fun createEvent(transactionType: TransactionType, blockHash: String, chain: ChainId = defaultChainId) =
        Event(
            UUID.randomUUID(), chain.value, "fromAddress", "toAddress", "contractAddress",
            "issuer", "txHash", transactionType, 10, "assetName", "symbol", 100, blockHash,
            Instant.now().toEpochMilli(), BigInteger.TEN, 18,
            null, null, null, 6
        )

    private class TestContext {
        lateinit var task: Task
        lateinit var anotherTask: Task
    }
}
