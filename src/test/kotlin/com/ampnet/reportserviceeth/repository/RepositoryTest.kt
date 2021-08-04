package com.ampnet.reportserviceeth.repository

import com.ampnet.reportserviceeth.TestBase
import com.ampnet.reportserviceeth.blockchain.TransactionType
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
import java.util.UUID

@ExtendWith(SpringExtension::class)
@DataJpaTest
@Import(DatabaseCleanerService::class)
class RepositoryTest : TestBase() {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var databaseCleanerService: DatabaseCleanerService

    private val txHash = "0xab059a62e22e230fe0f56d8555340a29b2e9532360368f810595453f6fdd213b"
    private val logIndex = 23L
    private val blockHash = "0x8243343df08b9751f5ca0c5f8c9c0460d8a9b6351066fae0acbd4d3e776de8bb"

    @BeforeEach
    fun init() {
        databaseCleanerService.deleteAllEvents()
        databaseCleanerService.deleteAllTasks()
    }

    @Test
    fun mustNotBeAbleToSaveDuplicateEvent() {
        eventRepository.saveAndFlush(createEvent(txHash, logIndex, blockHash))
        assertThrows<RuntimeException> { eventRepository.saveAndFlush(createEvent(txHash, logIndex, blockHash)) }
    }

    @Test
    fun mustBeAbleToSaveTask() {
        taskRepository.save(createTask())
    }

    private fun createEvent(txHash: String = "txHash", logIndex: Long = 134L, blockHash: String = "blockHash") =
        Event(
            UUID.randomUUID(), 5L, "addressFrom", "addressTo",
            "contractAddress", txHash, TransactionType.RESERVE_INVESTMENT,
            logIndex, "asset_name", 500045L, blockHash,
            1628065107449L, BigInteger("500"), BigInteger("500"), 50L, BigInteger("500")
        )

    private fun createTask() = Task(UUID.randomUUID(), 5075L, 1628065107449L)
}
