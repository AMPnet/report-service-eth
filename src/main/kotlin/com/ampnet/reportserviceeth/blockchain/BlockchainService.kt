package com.ampnet.reportserviceeth.blockchain

interface BlockchainService {
    fun getTransactions(wallet: String): List<TransactionInfo>
    fun getTransactionInfo(txHash: String): TransactionInfo
    fun getIssuerOwner(issuer: String): String
    fun getWhitelistedAddress(issuer: String): List<String>
}
