package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesHandler
import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesWithServices
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.exception.InvalidRequestException
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
     * Transaction receipt is fetched for the txHash and it contains `to` and `from` variables, asset address
     * and list of all the events.
     * In case of INVEST, CANCEL_INVESTMENT, CLAIM_TOKENS events `to` is the address of CfManagerSoftcap contract.
     * In case of REVENUE_SHARE event `to` is the address of PayoutManager contract.
     * Returns transactionInfo object which is mapped from the type of events available.
     * If transaction receipt not found for the txHash or doesn't contain any events returns InternalException.
     *
     * @param txHash String hash of the transaction
     * @param chainId Long id of the wanted chain
     * @return [TransactionInfo] object
     */
    @Suppress("ReturnCount")
    @Throws(InternalException::class, InvalidRequestException::class)
    override fun getTransactionInfo(txHash: String, chainId: Long): TransactionInfo {
        logger.debug { "Get info for transaction with hash: $txHash" }
        val chainProperties = chainPropertiesHandler.getBlockchainProperties(chainId)
        val txReceipt: TransactionReceipt = chainProperties.web3j.ethGetTransactionReceipt(txHash)
            .sendSafely()?.transactionReceipt?.unwrap() ?: throw InternalException(
            ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
            "Failed to fetch transaction info for txHash: $txHash"
        )
        if (txReceipt.to == null) throw InvalidRequestException(
            ErrorCode.BLOCKCHAIN_TX_NOT_A_CONTRACT_CALL, "$txHash is a contract creation transaction"
        )
        val contract = TransactionEvents.load(
            txReceipt.to,
            chainProperties.web3j,
            chainProperties.transactionManager,
            DefaultGasProvider()
        )
        skipException { contract.getInvestEvents(txReceipt) }?.firstOrNull()?.let {
            logger.debug { "Fetched reserve investment event for hash: $txHash" }
            return TransactionInfo(it, txReceipt, getAsset(it.asset, chainProperties))
        }
        skipException { contract.getCancelInvestmentEvents(txReceipt) }?.firstOrNull()?.let {
            logger.debug { "Fetched cancel investment event for hash: $txHash" }
            return TransactionInfo(it, txReceipt, getAsset(it.asset, chainProperties))
        }
        skipException { contract.getClaimEvents(txReceipt) }?.firstOrNull()?.let {
            logger.debug { "Fetched investment completed event for hash: $txHash" }
            return TransactionInfo(it, txReceipt, getAsset(it.asset, chainProperties))
        }
        skipException { contract.getReleaseEvents(txReceipt) }?.firstOrNull()?.let {
            logger.debug { "Fetched revenue share payout event for hash: $txHash" }
            return TransactionInfo(
                it, txReceipt, getAsset(it.asset, chainProperties)
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

    @Suppress("ThrowsCount")
    private fun getDeployedContractsForFetchingEvents(
        chainProperties: ChainPropertiesWithServices
    ): List<String> {
        val cfManagerFactoryContract = ICfManagerSoftcapFactory.load(
            chainProperties.chain.cfManagerFactoryAddress,
            chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        val payoutManagerFactoryContract = IPayoutManagerFactory.load(
            chainProperties.chain.payoutManagerFactoryAddress,
            chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        val payoutManagerInstances = payoutManagerFactoryContract.instances.sendSafely()?.mapNotNull { it as? String }
            ?: run {
                logger.debug {
                    "There are no contracts deployed for the payoutManagerFactory at: " +
                        chainProperties.chain.payoutManagerFactoryAddress
                }
                emptyList()
            }
        val cfManagerInstances = cfManagerFactoryContract.instances.sendSafely()?.mapNotNull { it as? String }
            ?: run {
                logger.debug {
                    "There are no contracts deployed for the cfManagerFactory at: " +
                        chainProperties.chain.cfManagerFactoryAddress
                }
                emptyList()
            }
        return cfManagerInstances.plus(payoutManagerInstances).ifEmpty {
            throw InternalException(
                ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
                "There are no contracts deployed to fetch events"
            )
        }
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private fun <T> skipException(action: () -> T): T? {
        return try {
            action()
        } catch (ex: Exception) {
            null
        }
    }

    private fun getAsset(contractAddress: String, chainProperties: ChainPropertiesWithServices): IAsset.AssetState {
        val assetContract = IAsset.load(
            contractAddress, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        return assetContract.state.sendSafely() ?: throw InternalException(
            ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
            "Cannot find the asset for address: $contractAddress"
        )
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
            chainProperties.chain.payoutManagerFactoryAddress,
            chainProperties.web3j,
            chainProperties.transactionManager,
            DefaultGasProvider()
        )
        val logsMap: Map<Log, Log> = logs.associateWith { it }
        skipException { contract.getInvestEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(it.asset, chainProperties)
            events.add(Event(it, chainId, log, asset))
        }
        skipException { contract.getCancelInvestmentEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(it.asset, chainProperties)
            events.add(Event(it, chainId, log, asset))
        }
        skipException { contract.getClaimEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(it.asset, chainProperties)
            events.add(Event(it, chainId, log, asset))
        }
        skipException { contract.getCreatePayoutEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(it.asset, chainProperties)
            events.add(Event(it, chainId, log, asset))
        }
        skipException { contract.getReleaseEvents(txReceipt) }?.forEach {
            val log = getLog(logsMap, it)
            val asset = getAsset(it.asset, chainProperties)
            events.add(Event(it, chainId, log, asset))
        }
        return events
    }

    private fun getLog(logsMap: Map<Log, Log>, event: BaseEventResponse) =
        logsMap[event.log] ?: throw InternalException(
            ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
            "Cannot find the log for contract address: ${event.log.address} inside the logsMap."
        )
}
