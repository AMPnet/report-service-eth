package com.ampnet.reportserviceeth.blockchain

import TransactionEvents
import com.ampnet.reportserviceeth.config.ApplicationProperties
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.service.sendSafely
import com.ampnet.reportserviceeth.service.unwrap
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.http.HttpService
import org.web3j.tx.ReadonlyTransactionManager
import org.web3j.tx.gas.DefaultGasProvider

private val logger = KotlinLogging.logger {}

@Service
class BlockchainServiceImpl(private val applicationProperties: ApplicationProperties) : BlockchainService {

    private val web3j by lazy { Web3j.build(HttpService(applicationProperties.provider.blockchainApi)) }
    private val readonlyTransactionManager = ReadonlyTransactionManager(
        web3j, applicationProperties.smartContract.walletAddress
    )

    override fun getTransactions(wallet: String): List<TransactionInfo> {
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
     * @return [TransactionInfo] object
     */
    @Suppress("ReturnCount")
    override fun getTransactionInfo(txHash: String): TransactionInfo {
        logger.debug { "Get info for transaction with hash: $txHash" }
        val txReceipt: TransactionReceipt = web3j.ethGetTransactionReceipt(txHash)
            .sendSafely()?.transactionReceipt?.unwrap() ?: throw InternalException(
            ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
            "Failed to fetch transaction info for txHash: $txHash"
        )
        val contract = TransactionEvents.load(txReceipt.to, web3j, readonlyTransactionManager, DefaultGasProvider())
        val asset = getAssetNameViaCfManagerContract(txReceipt.to)
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
            txReceipt.from, web3j, readonlyTransactionManager, DefaultGasProvider()
        )
        getEvents { payoutManagerContract.getCreatePayoutEvents(txReceipt) }?.firstOrNull()?.let {
            logger.debug { "Fetched revenue share payout even for hash: $txHash" }
            return TransactionInfo(it, txReceipt, getAssetNameViaPayoutManagerContract(txReceipt.from))
        }
        throw InternalException(ErrorCode.INT_JSON_RPC_BLOCKCHAIN, "Failed to map transaction info for txHash: $txHash")
    }

    @Suppress("TooGenericExceptionCaught")
    override fun getIssuerOwner(issuer: String): String {
        logger.debug { "Get owner of issuer: $issuer" }
        val contract = IIssuer.load(issuer, web3j, readonlyTransactionManager, DefaultGasProvider())
        return try {
            contract.state.send().owner
        } catch (ex: Exception) {
            throw InternalException(
                ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
                "Failed to fetch issuer owner address for contract address: $issuer", ex
            )
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override fun getWhitelistedAddress(issuer: String): List<String> {
        logger.debug { "Get whitelisted accounts for issuer: $issuer" }
        val contract = IIssuer.load(issuer, web3j, readonlyTransactionManager, DefaultGasProvider())
        return try {
            val addresses = contract.walletRecords.send() as List<IIssuer.WalletRecord>
            addresses.filter { it.whitelisted }.map { it.wallet }
        } catch (ex: Exception) {
            throw InternalException(
                ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
                "Failed to fetch whitelisted addresses for issuer contract address: $issuer", ex
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

    private fun getAssetNameViaCfManagerContract(contractAddress: String): String? {
        val cfManagerContract = ICfManagerSoftcap.load(
            contractAddress, web3j, readonlyTransactionManager, DefaultGasProvider()
        )
        val assetContractAddress = cfManagerContract.state.sendSafely()?.asset ?: return null
        return getAssetName(assetContractAddress)
    }

    private fun getAssetNameViaPayoutManagerContract(contractAddress: String): String? {
        val payoutManagerContract = IPayoutManager.load(
            contractAddress, web3j, readonlyTransactionManager, DefaultGasProvider()
        )
        val assetContractAddress = payoutManagerContract.state.sendSafely()?.asset ?: return null
        return getAssetName(assetContractAddress)
    }

    private fun getAssetName(assetContractAddress: String): String? {
        val assetContract = IAsset.load(assetContractAddress, web3j, readonlyTransactionManager, DefaultGasProvider())
        return assetContract.state.sendSafely()?.name
    }
}
