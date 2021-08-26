package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesHandler
import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesWithServices
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.persistence.model.Event
import com.ampnet.reportserviceeth.service.sendSafely
import com.ampnet.reportserviceeth.service.unwrap
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
import kotlin.jvm.Throws

private val logger = KotlinLogging.logger {}

@Service
class BlockchainEventServiceImpl(
    private val chainPropertiesHandler: ChainPropertiesHandler
) : BlockchainEventService {

    /**
     * Transaction receipt is fetched for the txHash and it contains `to` and `from` variables
     * and list of all the events.
     * In case of INVEST, CANCEL_INVESTMENT, CLAIM_TOKENS events `to` is the address of CfManagerSoftcap contract.
     * In case of REVENUE_SHARE event `from` is the address of PayoutManager contract.
     * Both of these contracts contain state pointing to Asset contract which is used to fetch the asset name.
     * Returns transactionInfo object which is mapped from the type of events available.
     * If transaction receipt not found for the txHash or doesn't contain any events returns InternalException.
     *
     * @param txHash String hash of the transaction
     * @param chainId Long id of the wanted chain
     * @return [TransactionInfo] object
     */
    @Suppress("ReturnCount")
    override fun getTransactionInfo(txHash: String, chainId: Long): TransactionInfo {
        logger.debug { "Get info for transaction with hash: $txHash" }
        val chainProperties = chainPropertiesHandler.getBlockchainProperties(chainId)
        val txReceipt: TransactionReceipt = chainProperties.web3j.ethGetTransactionReceipt(txHash)
            .sendSafely()?.transactionReceipt?.unwrap() ?: throw InternalException(
            ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
            "Failed to fetch transaction info for txHash: $txHash"
        )
        val contract = TransactionEvents.load(
            txReceipt.to,
            chainProperties.web3j,
            chainProperties.transactionManager,
            DefaultGasProvider()
        )
        val asset = getAssetStateViaCfManagerContract(txReceipt.to, chainProperties)
        getEvents { contract.getInvestEvents(txReceipt) }?.firstOrNull()?.let {
            logger.debug { "Fetched reserve investment event for hash: $txHash" }
            return TransactionInfo(it, txReceipt, asset)
        }
        getEvents { contract.getCancelInvestmentEvents(txReceipt) }?.firstOrNull()?.let {
            logger.debug { "Fetched cancel investment event for hash: $txHash" }
            return TransactionInfo(it, txReceipt, asset)
        }
        getEvents { contract.getClaimEvents(txReceipt) }?.firstOrNull()?.let {
            logger.debug { "Fetched investment completed event for hash: $txHash" }
            return TransactionInfo(it, txReceipt, asset)
        }
        val payoutManagerContract = TransactionEvents.load(
            txReceipt.from, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        getEvents { payoutManagerContract.getCreatePayoutEvents(txReceipt) }?.firstOrNull()?.let {
            logger.debug { "Fetched revenue share payout event for hash: $txHash" }
            return TransactionInfo(
                it, txReceipt, getAssetStateViaPayoutManagerContract(txReceipt.from, chainProperties)
            )
        }
        throw InternalException(ErrorCode.INT_JSON_RPC_BLOCKCHAIN, "Failed to map transaction info for txHash: $txHash")
    }

    @Throws(InternalException::class)
    override fun getAllEvents(startBlockNumber: Long, endBlockNumber: Long, chainId: Long): List<Event> {
        val chainProperties = chainPropertiesHandler.getBlockchainProperties(chainId)
        val deployedContracts = getDeployedContractsForFetchingEvents(chainProperties)
        val ethFilter = EthFilter(
            DefaultBlockParameter.valueOf(BigInteger.valueOf(startBlockNumber)),
            DefaultBlockParameter.valueOf(BigInteger.valueOf(endBlockNumber)),
            deployedContracts.keys.toList()
        )
        val ethLog: EthLog = chainProperties.web3j.ethGetLogs(ethFilter).sendSafely()
            ?: throw InternalException(
                ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
                "Failed to fetch events from $startBlockNumber to $endBlockNumber block, " +
                    "for contracts ${deployedContracts.keys.joinToString()}"
            )
        val logs = ethLog.logs.mapNotNull { it.get() as? EthLog.LogObject }
        return generateEvents(logs, deployedContracts, chainProperties, chainId)
    }

    @Suppress("ThrowsCount")
    private fun getDeployedContractsForFetchingEvents(
        chainProperties: ChainPropertiesWithServices
    ): Map<String, IAsset.AssetState> {
        val cfManagerFactoryContract = ICfManagerSoftcapFactory.load(
            chainProperties.chain.cfManagerFactoryAddress,
            chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        val payoutManagerFactoryContract = IPayoutManagerFactory.load(
            chainProperties.chain.payoutManagerFactoryAddress,
            chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        val cfManagerInstances = cfManagerFactoryContract.instances.sendSafely()?.associate {
            val contractAddress = it as String
            val assetState = getAssetStateViaCfManagerContract(contractAddress, chainProperties)
                ?: throw InternalException(
                    ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
                    "Failed to fetch assetState for cfManagerContract: $contractAddress"
                )
            contractAddress to assetState
        } ?: run {
            logger.debug {
                "There are no contracts deployed for the cfManagerFactory at: " +
                    chainProperties.chain.cfManagerFactoryAddress
            }
            mapOf()
        }
        val payoutManagerInstances = payoutManagerFactoryContract.instances
            .sendSafely()?.associate {
                val contractAddress = it as String
                val assetState = getAssetStateViaPayoutManagerContract(contractAddress, chainProperties)
                    ?: throw InternalException(
                        ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
                        "Failed to fetch assetState for payoutManagerContract: $contractAddress"
                    )
                contractAddress to assetState
            } ?: run {
            logger.debug {
                "There are no contracts deployed for the payoutManagerFactory at: " +
                    chainProperties.chain.payoutManagerFactoryAddress
            }
            mapOf()
        }
        return cfManagerInstances.plus(payoutManagerInstances).ifEmpty {
            throw InternalException(
                ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
                "There are no contracts deployed to fetch events"
            )
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun <T> getEvents(action: () -> T): T? {
        return try {
            action()
        } catch (ex: Exception) {
            logger.info("Failed to fetch events", ex)
            null
        }
    }

    private fun getAssetStateViaCfManagerContract(
        contractAddress: String,
        chainProperties: ChainPropertiesWithServices
    ): IAsset.AssetState? {
        val cfManagerContract = ICfManagerSoftcap.load(
            contractAddress, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        val assetContractAddress = cfManagerContract.state.sendSafely()?.asset ?: return null
        return getAsset(assetContractAddress, chainProperties)
    }

    private fun getAssetStateViaPayoutManagerContract(
        contractAddress: String,
        chainProperties: ChainPropertiesWithServices
    ): IAsset.AssetState? {
        val payoutManagerContract = IPayoutManager.load(
            contractAddress, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        val assetContractAddress = payoutManagerContract.state.sendSafely()?.asset ?: return null
        return getAsset(assetContractAddress, chainProperties)
    }

    private fun getAsset(contractAddress: String, chainProperties: ChainPropertiesWithServices): IAsset.AssetState? {
        val assetContract = IAsset.load(
            contractAddress, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        return assetContract.state.sendSafely()
    }

    private fun generateEvents(
        logs: List<Log>,
        deployedContracts: Map<String, IAsset.AssetState>,
        chainProperties: ChainPropertiesWithServices,
        chainId: Long
    ): List<Event> {
        val events = mutableListOf<Event>()
        val txReceipt = TransactionReceipt().apply { this.logs = logs }
        // The contract address is not important since it doesn't fetch anything from the blockchain.
        // It is only used to map logs to events.
        val contract = TransactionEvents.load(
            chainProperties.chain.payoutManagerFactoryAddress,
            chainProperties.web3j,
            chainProperties.transactionManager,
            DefaultGasProvider()
        )
        val logsMap: Map<Log, Log> = logs.associateWith { it }
        getEvents { contract.getInvestEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(deployedContracts, log)
            events.add(Event(it, chainId, log, asset))
        }
        getEvents { contract.getCancelInvestmentEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(deployedContracts, log)
            events.add(Event(it, chainId, log, asset))
        }
        getEvents { contract.getClaimEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(deployedContracts, log)
            events.add(Event(it, chainId, log, asset))
        }
        getEvents { contract.getCreatePayoutEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(deployedContracts, log)
            events.add(Event(it, chainId, log, asset))
        }
        return events
    }

    private fun getLog(logsMap: Map<Log, Log>, event: BaseEventResponse) =
        logsMap[event.log] ?: throw InternalException(
            ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
            "Cannot find the log for contract address: ${event.log.address} inside the logsMap."
        )

    private fun getAsset(deployedContracts: Map<String, IAsset.AssetState>, log: Log) =
        deployedContracts[log.address]?.name ?: throw InternalException(
            ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
            "Cannot find the asset for address: ${log.address} inside the deployedContracts."
        )
}
