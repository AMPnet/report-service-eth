package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesHandler
import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesWithServices
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.persistence.model.Event
import com.ampnet.reportserviceeth.service.sendSafely
import com.ampnet.reportserviceth.contract.IAssetCommon
import com.ampnet.reportserviceth.contract.ICampaignFactoryCommon
import com.ampnet.reportserviceth.contract.IIssuerCommon
import com.ampnet.reportserviceth.contract.ISnapshotDistributorFactoryCommon
import com.ampnet.reportserviceth.contract.IToken
import com.ampnet.reportserviceth.contract.TransactionEvents
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.core.methods.response.BaseEventResponse
import org.web3j.protocol.core.methods.response.EthLog
import org.web3j.protocol.core.methods.response.Log
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger
import java.util.concurrent.ConcurrentHashMap

private val logger = KotlinLogging.logger {}

@Service
class BlockchainEventServiceImpl(
    private val chainPropertiesHandler: ChainPropertiesHandler
) : BlockchainEventService {

    private data class CacheKey(val address: String, val chainId: Long)
    private val assetCache: MutableMap<CacheKey, IAssetCommon.AssetCommonState> = ConcurrentHashMap()
    private val stableCoinPrecisionCache: MutableMap<CacheKey, BigInteger> = ConcurrentHashMap()

    @Throws(InternalException::class)
    override fun getAllEvents(startBlockNumber: Long, endBlockNumber: Long, chainId: Long): List<Event> {
        val chainProperties = chainPropertiesHandler.getBlockchainProperties(chainId)
        val deployedContracts = getDeployedContractsForFetchingEvents(chainProperties)
        if (deployedContracts.isEmpty()) return emptyList()

        val ethFilter = EthFilter(
            DefaultBlockParameter.valueOf(BigInteger.valueOf(startBlockNumber)),
            DefaultBlockParameter.valueOf(BigInteger.valueOf(endBlockNumber)),
            deployedContracts
        )
        val ethLog: EthLog = chainProperties.web3j.ethGetLogs(ethFilter).sendSafely()
            ?: throw InternalException(
                ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
                "Failed to fetch events from $startBlockNumber to $endBlockNumber block, " +
                    "for contracts ${deployedContracts.joinToString()}"
            )
        val logs = ethLog.logs.mapNotNull { it.get() as? EthLog.LogObject }
        return generateEvents(logs, chainProperties, chainId)
    }

    private fun getDeployedContractsForFetchingEvents(
        chainProperties: ChainPropertiesWithServices
    ): List<String> {
        val snapshotInstances: List<String> = chainProperties.chain.snapshotDistributorAddresses.map { address ->
            val snapshotFactoryCommon = ISnapshotDistributorFactoryCommon.load(
                address, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
            )
            snapshotFactoryCommon.instances.sendSafely()?.mapNotNull { it as? String }.orEmpty()
        }.flatten()
        if (snapshotInstances.isEmpty()) {
            logger.info {
                "There are no contracts deployed for the payoutManagerFactory address: " +
                    chainProperties.chain.snapshotDistributorAddresses
            }
        }
        val cfManagerInstances: List<String> = chainProperties.chain.cfManagerFactoryAddresses.map { address ->
            val cfManagerFactoryContract = ICampaignFactoryCommon.load(
                address, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
            )
            cfManagerFactoryContract.instances.sendSafely()?.mapNotNull { it as? String }.orEmpty()
        }.flatten()
        if (cfManagerInstances.isEmpty()) {
            logger.info {
                "There are no contracts deployed for the cfManagerFactory address: " +
                    chainProperties.chain.cfManagerFactoryAddresses
            }
        }
        return cfManagerInstances.plus(snapshotInstances)
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private fun <T> skipException(action: () -> T): T? {
        return try {
            action()
        } catch (ex: Exception) {
            logger.debug { "There was an exception while fetching events: ${ex.message}" }
            null
        }
    }

    private fun generateEvents(
        logs: List<Log>,
        chainProperties: ChainPropertiesWithServices,
        chainId: Long
    ): List<Event> {
        val events = mutableListOf<Event>()
        val txReceipt = TransactionReceipt().apply { this.logs = logs }
        // The contract address is not important since it doesn't fetch anything from the blockchain.
        // It is only used to map logs to events.
        val contract = TransactionEvents.load(
            chainProperties.chain.callerAddress,
            chainProperties.web3j,
            chainProperties.transactionManager,
            DefaultGasProvider()
        )
        val logsMap: Map<Log, Log> = logs.associateWith { it }
        skipException { contract.getInvestEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(it.asset, chainProperties)
            val stableCoinPrecision = getStableCoinPrecision(asset.issuer, chainProperties)
            events.add(Event(it, chainId, log, asset, stableCoinPrecision))
        }
        skipException { contract.getCancelInvestmentEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(it.asset, chainProperties)
            val stableCoinPrecision = getStableCoinPrecision(asset.issuer, chainProperties)
            events.add(Event(it, chainId, log, asset, stableCoinPrecision))
        }
        skipException { contract.getClaimEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(it.asset, chainProperties)
            val stableCoinPrecision = getStableCoinPrecision(asset.issuer, chainProperties)
            events.add(Event(it, chainId, log, asset, stableCoinPrecision))
        }
        skipException { contract.getCreatePayoutEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(it.asset, chainProperties)
            val stableCoinPrecision = getStableCoinPrecision(asset.issuer, chainProperties)
            events.add(Event(it, chainId, log, asset, stableCoinPrecision))
        }
        skipException { contract.getReleaseEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(it.asset, chainProperties)
            val stableCoinPrecision = getStableCoinPrecision(asset.issuer, chainProperties)
            events.add(Event(it, chainId, log, asset, stableCoinPrecision))
        }
        return events
    }

    private fun getLog(logsMap: Map<Log, Log>, event: BaseEventResponse) =
        logsMap[event.log] ?: throw InternalException(
            ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
            "Cannot find the log for contract address: ${event.log.address} inside the logsMap."
        )

    private fun getAsset(
        contractAddress: String,
        chainProperties: ChainPropertiesWithServices
    ): IAssetCommon.AssetCommonState {
        val cacheKey = CacheKey(contractAddress, chainProperties.chainId)
        assetCache[cacheKey]?.let { return it }
        val assetContract = IAssetCommon.load(
            contractAddress, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        val commonState = assetContract.commonState().sendSafely() ?: throw InternalException(
            ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
            "Cannot find the asset for address: $contractAddress"
        )
        assetCache[cacheKey] = commonState
        return commonState
    }

    private fun getStableCoinPrecision(
        issuerAddress: String,
        chainProperties: ChainPropertiesWithServices
    ): BigInteger {
        val cacheKey = CacheKey(issuerAddress, chainProperties.chainId)
        stableCoinPrecisionCache[cacheKey]?.let { return it }
        val issuer = IIssuerCommon.load(
            issuerAddress, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        val stableCoinAddress = issuer.commonState().sendSafely()?.stablecoin
        val stableCoin = IToken.load(
            stableCoinAddress,
            chainProperties.web3j,
            chainProperties.transactionManager,
            DefaultGasProvider()
        )
        val stableCoinPrecision = stableCoin.decimals()?.sendSafely() ?: throw InternalException(
            ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
            "Cannot get stable coin decimals for issuer address: $issuerAddress"
        )
        stableCoinPrecisionCache[cacheKey] = stableCoinPrecision
        return stableCoinPrecision
    }
}
