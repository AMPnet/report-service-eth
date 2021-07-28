package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.config.ApplicationProperties
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

    override fun getTransactions(wallet: String): List<TransactionInfo> {
        logger.debug { "Get transactions for wallet address: $wallet" }
        if (wallet.isBlank()) return emptyList()
        TODO("Not implemented")
    }

    override fun getTransactionInfo(txHash: String): TransactionInfo {
        logger.debug { "Get info for transaction with hash: $txHash" }
        TODO("Not implemented")
    }

    override fun getIssuerOwner(issuer: String): String {
        logger.debug { "Get owner of issuer: $issuer" }
        TODO("Not implemented")
    }

    override fun getWhitelistedAddress(issuer: String): List<String> {
        logger.debug { "Get whitelisted accounts for issuer: $issuer" }
        val transactionManager = ReadonlyTransactionManager(web3j, applicationProperties.smartContract.walletAddress)
        val contract = IIssuer.load(issuer, web3j, transactionManager, DefaultGasProvider())
        val addresses = contract.walletRecords.send() as List<IIssuer.WalletRecord>
        return addresses.filter { it.whitelisted }.map { it.wallet }
    }
}
