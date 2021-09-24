package com.ampnet.reportserviceeth.service.impl

import com.ampnet.reportserviceeth.blockchain.BlockchainEventService
import com.ampnet.reportserviceeth.blockchain.BlockchainService
import com.ampnet.reportserviceeth.blockchain.properties.Chain
import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesHandler
import com.ampnet.reportserviceeth.config.ApplicationProperties
import com.ampnet.reportserviceeth.config.ChainProperties
import com.ampnet.reportserviceeth.persistence.model.Task
import com.ampnet.reportserviceeth.persistence.repository.EventRepository
import com.ampnet.reportserviceeth.persistence.repository.TaskRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger {}

@Service
class EventQueueServiceImpl(
    applicationProperties: ApplicationProperties,
    private val taskRepository: TaskRepository,
    private val eventRepository: EventRepository,
    private val blockchainService: BlockchainService,
    private val blockchainEventService: BlockchainEventService,
    private val chainPropertiesHandler: ChainPropertiesHandler
) {

    init {
        val activeChains = Chain.values().mapNotNull { chain ->
            chainPropertiesHandler.getChainProperties(chain)?.let { properties ->
                Pair(chain, properties)
            }
        }
        val executorService = Executors.newScheduledThreadPool(activeChains.size)
        activeChains.forEach {
            executorService.scheduleAtFixedRate(
                { processTask(it.first, it.second) },
                applicationProperties.queue.initialDelay,
                applicationProperties.queue.polling,
                TimeUnit.MILLISECONDS
            )
        }
    }

    @Transactional
    @Suppress("TooGenericExceptionCaught")
    fun processTask(chain: Chain, chainProperties: ChainProperties) {
        val task = taskRepository.findByChainId(chain.id)
        val startBlockNumber = task?.let { it.blockNumber + 1 } ?: chainProperties.startBlockNumber
        try {
            val latestBlockNumber = blockchainService.getBlockNumber(chain.id)
            val endBlockNumber = calculateEndBlockNumber(
                startBlockNumber, latestBlockNumber.toLong(), chainProperties
            )
            if (startBlockNumber >= endBlockNumber) {
//                logger.debug { "End block: $endBlockNumber is smaller than start block: $startBlockNumber" }
                return
            }
            val events = blockchainEventService.getAllEvents(startBlockNumber, endBlockNumber, chain.id)
            if (events.isNotEmpty()) {
                logger.debug { "Number of fetched events: ${events.size}" }
                eventRepository.saveAll(events)
            }
            val updatedTask = task?.apply {
                this.blockNumber = endBlockNumber
                this.timestamp = Instant.now().toEpochMilli()
            } ?: Task(chain.id, endBlockNumber)
            taskRepository.save(updatedTask)
        } catch (ex: Throwable) {
            logger.error { "Failed to fetch blockchain events: ${ex.message}" }
        }
    }

    private fun calculateEndBlockNumber(
        startBlockNumber: Long,
        latestBlockNumber: Long,
        chainProperties: ChainProperties
    ): Long =
        if (
            (latestBlockNumber - chainProperties.numOfConfirmations - startBlockNumber) > chainProperties.maxBlocks
        ) {
            startBlockNumber + chainProperties.maxBlocks
        } else {
            latestBlockNumber - chainProperties.numOfConfirmations
        }
}
