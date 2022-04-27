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
import com.ampnet.reportserviceeth.util.BlockNumber
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigInteger
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
        val task = taskRepository.findByChainId(chain.id.value)
        val startBlockNumber = BlockNumber(
            task?.let { BigInteger.valueOf(it.blockNumber + 1L) } ?: chainProperties.startBlockNumber
        )
        try {
            val latestBlockNumber = blockchainService.getBlockNumber(chain.id)
            val endBlockNumber = calculateEndBlockNumber(
                startBlockNumber, latestBlockNumber, chainProperties
            )
            if (startBlockNumber.value >= endBlockNumber.value) {
                return
            }
            val events = blockchainEventService.getAllEvents(startBlockNumber, endBlockNumber, chain.id)
            if (events.isNotEmpty()) {
                logger.debug { "Number of fetched events: ${events.size} on chainId: ${chain.id}" }
                eventRepository.saveAll(events)
            }
            val updatedTask = task?.apply {
                this.blockNumber = endBlockNumber.value.longValueExact()
                this.timestamp = Instant.now().toEpochMilli()
            } ?: Task(chain.id.value, endBlockNumber.value.longValueExact())
            taskRepository.save(updatedTask)
        } catch (ex: Throwable) {
            logger.warn { "Failed to fetch blockchain events: ${ex.message} on chainId: ${chain.id}" }
        }
    }

    private fun calculateEndBlockNumber(
        startBlockNumber: BlockNumber,
        latestBlockNumber: BlockNumber,
        chainProperties: ChainProperties
    ): BlockNumber {
        val diff = latestBlockNumber.value - chainProperties.numOfConfirmations - startBlockNumber.value

        return if (diff > chainProperties.maxBlocks) {
            BlockNumber(startBlockNumber.value + chainProperties.maxBlocks)
        } else {
            BlockNumber(latestBlockNumber.value - chainProperties.numOfConfirmations)
        }
    }
}
