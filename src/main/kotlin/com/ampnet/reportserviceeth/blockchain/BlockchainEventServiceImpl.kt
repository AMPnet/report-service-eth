package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesHandler
import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesWithServices
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.persistence.model.Event
import com.ampnet.reportserviceeth.service.sendSafely
import com.ampnet.reportserviceeth.util.BlockNumber
import com.ampnet.reportserviceeth.util.ChainId
import com.ampnet.reportserviceeth.util.ContractAddress
import com.ampnet.reportserviceth.contract.ERC20Detailed
import com.ampnet.reportserviceth.contract.IAssetCommon
import com.ampnet.reportserviceth.contract.ICampaignFactoryCommon
import com.ampnet.reportserviceth.contract.IIssuerCommon
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

    private data class CacheKey(val address: ContractAddress, val chainId: ChainId)

    private val assetCache: MutableMap<CacheKey, IAssetCommon.AssetCommonState> = ConcurrentHashMap()
    private val stableCoinPrecisionCache: MutableMap<CacheKey, BigInteger> = ConcurrentHashMap()

    @Throws(InternalException::class)
    override fun getAllEvents(
        startBlockNumber: BlockNumber,
        endBlockNumber: BlockNumber,
        chainId: ChainId
    ): List<Event> {
        val chainProperties = chainPropertiesHandler.getBlockchainProperties(chainId)
        val deployedContracts = getDeployedContractsForFetchingEvents(chainProperties)
        if (deployedContracts.isEmpty()) return emptyList()

        val ethFilter = EthFilter(
            DefaultBlockParameter.valueOf(startBlockNumber.value),
            DefaultBlockParameter.valueOf(endBlockNumber.value),
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
        val payoutMangerInstances = chainProperties.chain.payoutManagerAddresses
        if (payoutMangerInstances.isEmpty()) {
            logger.info { "There are no payout manager contracts specified" }
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
        return cfManagerInstances.plus(payoutMangerInstances)
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
        chainId: ChainId
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
            val asset = getAsset(ContractAddress(it.asset), chainProperties)
            val stableCoinPrecision = getStableCoinPrecision(ContractAddress(asset.issuer), chainProperties)
            events.add(Event(it, chainId.value, log, asset, stableCoinPrecision))
        }
        skipException { contract.getCancelInvestmentEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(ContractAddress(it.asset), chainProperties)
            val stableCoinPrecision = getStableCoinPrecision(ContractAddress(asset.issuer), chainProperties)
            events.add(Event(it, chainId.value, log, asset, stableCoinPrecision))
        }
        skipException { contract.getClaimEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(ContractAddress(it.asset), chainProperties)
            val stableCoinPrecision = getStableCoinPrecision(ContractAddress(asset.issuer), chainProperties)
            events.add(Event(it, chainId.value, log, asset, stableCoinPrecision))
        }
        skipException { contract.getPayoutCreatedEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(ContractAddress(it.rewardAsset), chainProperties)
            val stableCoinPrecision = getStableCoinPrecision(ContractAddress(asset.issuer), chainProperties)
            events.add(Event(it, chainId.value, log, asset, stableCoinPrecision))
        }
        skipException { contract.getPayoutCanceledEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(ContractAddress(it.rewardAsset), chainProperties)
            val stableCoinPrecision = getStableCoinPrecision(ContractAddress(asset.issuer), chainProperties)
            events.add(Event(it, chainId.value, log, asset, stableCoinPrecision))
        }
        skipException { contract.getPayoutClaimedEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(ContractAddress(it.rewardAsset), chainProperties)
            val stableCoinPrecision = getStableCoinPrecision(ContractAddress(asset.issuer), chainProperties)
            events.add(Event(it, chainId.value, log, asset, stableCoinPrecision))
        }
//        skipException { contract.getReleaseEvents(txReceipt) }?.forEach {
//            val log = getLog(logsMap, it)
//            val asset = getAsset(ContractAddress(it.asset), chainProperties)
//            val stableCoinPrecision = getStableCoinPrecision(ContractAddress(asset.issuer), chainProperties)
//            events.add(Event(it, chainId.value, log, asset, stableCoinPrecision))
//        }
        return events
    }

    private fun getLog(logsMap: Map<Log, Log>, event: BaseEventResponse) =
        logsMap[event.log] ?: throw InternalException(
            ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
            "Cannot find the log for contract address: ${event.log.address} inside the logsMap."
        )

    private fun getAsset(
        contractAddress: ContractAddress,
        chainProperties: ChainPropertiesWithServices
    ): IAssetCommon.AssetCommonState {
        val cacheKey = CacheKey(contractAddress, chainProperties.chainId)
        assetCache[cacheKey]?.let { return it }
        val assetContract = IAssetCommon.load(
            contractAddress.value, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        val commonState = assetContract.commonState().sendSafely() ?: throw InternalException(
            ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
            "Cannot find the asset for address: $contractAddress"
        )
        assetCache[cacheKey] = commonState
        return commonState
    }

    private fun getStableCoinPrecision(
        issuerAddress: ContractAddress,
        chainProperties: ChainPropertiesWithServices
    ): BigInteger {
        val cacheKey = CacheKey(issuerAddress, chainProperties.chainId)
        stableCoinPrecisionCache[cacheKey]?.let { return it }
        val issuer = IIssuerCommon.load(
            issuerAddress.value, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        val stableCoinAddress = issuer.commonState().sendSafely()?.stablecoin
        val stableCoin = ERC20Detailed.load(
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
