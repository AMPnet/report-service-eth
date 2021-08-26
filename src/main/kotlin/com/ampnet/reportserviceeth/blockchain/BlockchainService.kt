package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.persistence.model.Event
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import java.math.BigInteger

interface BlockchainService {
    fun getTransactions(wallet: String, chainId: Long): List<TransactionInfo>
    fun getTransactionInfo(txHash: String, chainId: Long): TransactionInfo

    @Throws(InternalException::class)
    fun getIssuerOwner(issuerRequest: IssuerRequest): String

    @Throws(InternalException::class)
    fun getWhitelistedAddress(issuerRequest: IssuerRequest): List<String>
    fun getAllEvents(startBlockNumber: Long, endBlockNumber: Long, chainId: Long): List<Event>
    fun getBlockNumber(chainId: Long): BigInteger
}
