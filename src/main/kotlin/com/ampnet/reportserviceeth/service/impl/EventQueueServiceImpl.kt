package com.ampnet.reportserviceeth.service.impl

import com.ampnet.reportserviceeth.blockchain.BlockchainEventService
import com.ampnet.reportserviceeth.blockchain.BlockchainService
import com.ampnet.reportserviceeth.blockchain.properties.Chain
import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesHandler
import com.ampnet.reportserviceeth.config.ApplicationProperties
import com.ampnet.reportserviceeth.config.ChainProperties
import com.ampnet.reportserviceeth.exception.InternalException
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
            { processTasks(Chain.MATIC_TESTNET_MUMBAI.id) },
            applicationProperties.queue.initialDelay,
            applicationProperties.queue.polling,
            TimeUnit.MILLISECONDS
        )
    }

    @Transactional
    fun processTasks(chainId: Long) {
        logger.debug { "Processing tasks for chainId: $chainId" }
        val chainProperties = chainPropertiesHandler.getBlockchainProperties(chainId)
        val startBlockNumber = taskRepository.findFirstByOrderByBlockNumberDesc()?.let { it.blockNumber + 1 }
            ?: chainProperties.chain.startBlockNumber
        logger.debug { "Start block number: $startBlockNumber" }
        try {
            val latestBlockNumber = blockchainService.getBlockNumber(chainId)
            logger.debug { "Latest block number is: $latestBlockNumber" }
            val endBlockNumber = calculateEndBlockNumber(
                startBlockNumber, latestBlockNumber.toLong(), chainProperties.chain
            )
            logger.debug { "End block number: $endBlockNumber" }
            if (startBlockNumber >= endBlockNumber) {
                logger.warn { "End block: $endBlockNumber is smaller than start block: $startBlockNumber" }
                return
            }
            val events = blockchainEventService.getAllEvents(startBlockNumber, endBlockNumber, chainId)
            logger.debug { "Number of fetched events: ${events.size}" }
            eventRepository.saveAll(events)
            taskRepository.save(Task(chainId, endBlockNumber))
        } catch (ex: InternalException) {
            logger.error { "Failed to fetch blockchain events: ${ex.message}" }
            return
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
