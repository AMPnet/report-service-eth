package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.service.data.IssuerRequest

interface BlockchainService {
    fun getTransactions(wallet: String, chainId: Long): List<TransactionInfo>
    fun getTransactionInfo(txHash: String, chainId: Long): TransactionInfo

    @Throws(InternalException::class)
    fun getIssuerOwner(issuerRequest: IssuerRequest): String

    @Throws(InternalException::class)
    fun getWhitelistedAddress(issuerRequest: IssuerRequest): List<String>
}
