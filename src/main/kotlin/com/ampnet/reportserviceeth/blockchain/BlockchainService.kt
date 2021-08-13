package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.service.data.IssuerRequest

interface BlockchainService {
    fun getTransactions(wallet: String, chainId: Long): List<TransactionInfo>
    fun getTransactionInfo(txHash: String, chainId: Long): TransactionInfo
    fun getIssuerOwner(issuerRequest: IssuerRequest): String
    fun getWhitelistedAddress(issuerRequest: IssuerRequest): List<String>
}
