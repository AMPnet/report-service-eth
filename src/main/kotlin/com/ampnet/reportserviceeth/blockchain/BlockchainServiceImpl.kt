package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.config.ApplicationProperties
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InternalException
import mu.KLogging
import org.springframework.stereotype.Service
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.ReadonlyTransactionManager
import org.web3j.tx.gas.DefaultGasProvider

@Service
class BlockchainServiceImpl(private val applicationProperties: ApplicationProperties) : BlockchainService {

    companion object : KLogging()

    private val web3j by lazy { Web3j.build(HttpService(applicationProperties.provider.blockchainApi)) }
    private val readonlyTransactionManager = ReadonlyTransactionManager(
        web3j, applicationProperties.smartContract.walletAddress
    )

    override fun getTransactions(wallet: String): List<TransactionInfo> {
        logger.debug { "Get transactions for wallet address: $wallet" }
        if (wallet.isBlank()) return emptyList()
        TODO("Not implemented")
    }

    override fun getTransactionInfo(txHash: String): TransactionInfo {
        logger.debug { "Get info for transaction with hash: $txHash" }
        TODO("Not implemented")
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
}
