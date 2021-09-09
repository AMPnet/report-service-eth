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

    private val executorService = Executors.newSingleThreadScheduledExecutor()

    init {
        executorService.scheduleAtFixedRate(
            { processTasksForChains() },
            applicationProperties.queue.initialDelay,
            applicationProperties.queue.polling,
            TimeUnit.MILLISECONDS
        )
    }

    @Transactional
    @Suppress("TooGenericExceptionCaught")
    fun processTask(chain: Chain, chainProperties: ChainProperties) {
        logger.debug { "Processing tasks for chainId: ${chain.id}" }
        val startBlockNumber = taskRepository.findFirstByBlockNumberForChain(chain.id)?.let { it.blockNumber + 1 }
            ?: chainProperties.startBlockNumber
        logger.debug { "Start block number: $startBlockNumber" }
        try {
            val latestBlockNumber = blockchainService.getBlockNumber(chain.id)
            logger.debug { "Latest block number is: $latestBlockNumber" }
            val endBlockNumber = calculateEndBlockNumber(
                startBlockNumber, latestBlockNumber.toLong(), chainProperties
            )
            logger.debug { "End block number: $endBlockNumber" }
            if (startBlockNumber >= endBlockNumber) {
                logger.warn { "End block: $endBlockNumber is smaller than start block: $startBlockNumber" }
                return
            }
            val events = blockchainEventService.getAllEvents(startBlockNumber, endBlockNumber, chain.id)
            logger.debug { "Number of fetched events: ${events.size}" }
            eventRepository.saveAll(events)
            taskRepository.save(Task(chain.id, endBlockNumber))
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

    private fun processTasksForChains() {
        Chain.values().forEach { chain ->
            chainPropertiesHandler.getChainProperties(chain)?.let { properties -> processTask(chain, properties) }
        }
    }
}
