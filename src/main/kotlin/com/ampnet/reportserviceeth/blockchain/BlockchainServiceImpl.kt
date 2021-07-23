package com.ampnet.reportserviceeth.blockchain

import mu.KLogging
import org.springframework.stereotype.Service

@Service
class BlockchainServiceImpl : BlockchainService {

    companion object : KLogging()

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
        TODO("Not yet implemented")
    }
}
