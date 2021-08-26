package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.blockchain.properties.Chain
import com.ampnet.reportserviceeth.config.ApplicationProperties
import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.persistence.model.Task
import com.ampnet.reportserviceeth.persistence.repository.EventRepository
import com.ampnet.reportserviceeth.persistence.repository.TaskRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger {}

@Service
class BlockchainQueueServiceImpl(
    private val applicationProperties: ApplicationProperties,
    private val taskRepository: TaskRepository,
    private val eventRepository: EventRepository,
    private val blockchainService: BlockchainService
) {

    private val executorService = Executors.newSingleThreadScheduledExecutor()

    init {
        executorService.scheduleAtFixedRate(
            { processTasks() },
            applicationProperties.queue.initialDelay,
            applicationProperties.queue.polling,
            TimeUnit.MILLISECONDS
        )
    }

    private fun processTasks() {
        val chainId = Chain.MATIC_MAIN.id
        val startBlockNumber = taskRepository.findFirstByOrderByBlockNumberDesc()?.let { it.blockNumber + 1 }
            ?: applicationProperties.queue.startBlockNumber
        try {
            val latestBlockNumber = blockchainService.getBlockNumber(chainId)
            val endBlockNumber = calculateEndBlockNumber(startBlockNumber, latestBlockNumber.toLong())
            val events = blockchainService.getAllEvents(startBlockNumber, endBlockNumber, chainId)
            eventRepository.saveAll(events)
            taskRepository.save(Task(chainId, endBlockNumber))
        } catch (ex: InternalException) {
            logger.error { "Failed to fetch blockchain events: ${ex.message}" }
            return
        }
    }

    private fun calculateEndBlockNumber(startBlockNumber: Long, latestBlockNumber: Long): Long {
        return if (
            (latestBlockNumber - applicationProperties.queue.numOfConfirmations - startBlockNumber)
        > applicationProperties.queue.maxBlocks
        ) {
            startBlockNumber + applicationProperties.queue.maxBlocks
        } else {
            latestBlockNumber - applicationProperties.queue.numOfConfirmations
        }
    }
}
