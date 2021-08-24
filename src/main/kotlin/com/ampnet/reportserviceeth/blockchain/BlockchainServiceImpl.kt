package com.ampnet.reportserviceeth.blockchain

import IAsset
import ICfManagerSoftcap
import IIssuer
import IPayoutManager
import TransactionEvents
import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesHandler
import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesWithServices
import com.ampnet.reportserviceeth.config.ApplicationProperties
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import com.ampnet.reportserviceeth.service.sendSafely
import com.ampnet.reportserviceeth.service.unwrap
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.gas.DefaultGasProvider

private val logger = KotlinLogging.logger {}

@Service
class BlockchainServiceImpl(applicationProperties: ApplicationProperties) : BlockchainService {

    private val chainHandler by lazy { ChainPropertiesHandler(applicationProperties) }

    override fun getTransactions(wallet: String, chainId: Long): List<TransactionInfo> {
        logger.debug { "Get transactions for wallet address: $wallet" }
        if (wallet.isBlank()) return emptyList()
        TODO("Not implemented")
    }

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
        val chainProperties = chainHandler.getBlockchainProperties(chainId)
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
            logger.debug { "Fetched reserve investment even for hash: $txHash" }
            return TransactionInfo(it, txReceipt, asset)
        }
        getEvents { contract.getCancelInvestmentEvents(txReceipt) }?.firstOrNull()?.let {
            logger.debug { "Fetched cancel investment even for hash: $txHash" }
            return TransactionInfo(it, txReceipt, asset)
        }
        getEvents { contract.getClaimEvents(txReceipt) }?.firstOrNull()?.let {
            logger.debug { "Fetched investment completed even for hash: $txHash" }
            return TransactionInfo(it, txReceipt, asset)
        }
        val payoutManagerContract = TransactionEvents.load(
            txReceipt.from, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        getEvents { payoutManagerContract.getCreatePayoutEvents(txReceipt) }?.firstOrNull()?.let {
            logger.debug { "Fetched revenue share payout even for hash: $txHash" }
            return TransactionInfo(
                it, txReceipt, getAssetStateViaPayoutManagerContract(txReceipt.from, chainProperties)
            )
        }
        throw InternalException(ErrorCode.INT_JSON_RPC_BLOCKCHAIN, "Failed to map transaction info for txHash: $txHash")
    }

    @Suppress("TooGenericExceptionCaught")
    override fun getIssuerOwner(issuerRequest: IssuerRequest): String {
        logger.debug { "Get owner of issuer: $issuerRequest" }
        val chainProperties = chainHandler.getBlockchainProperties(issuerRequest.chainId)
        val contract = IIssuer.load(
            issuerRequest.address, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        return contract.state.sendSafely()?.owner
            ?: throw InternalException(
                ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
                "Failed to fetch issuer owner address for contract address: $issuerRequest"
            )
    }

    @Suppress("TooGenericExceptionCaught")
    override fun getWhitelistedAddress(issuerRequest: IssuerRequest): List<String> {
        logger.debug { "Get whitelisted accounts for issuer: $issuerRequest" }
        val chainProperties = chainHandler.getBlockchainProperties(issuerRequest.chainId)
        val contract = IIssuer.load(
            issuerRequest.address, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        return contract.walletRecords.sendSafely()
            ?.filterIsInstance<IIssuer.WalletRecord>()
            ?.filter { it.whitelisted }
            ?.map { it.wallet }
            ?: throw InternalException(
                ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
                "Failed to fetch whitelisted addresses for issuer contract address: $issuerRequest"
            )
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
}
