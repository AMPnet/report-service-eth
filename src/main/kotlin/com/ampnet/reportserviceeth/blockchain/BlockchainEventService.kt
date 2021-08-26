package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.persistence.model.Event

interface BlockchainEventService {
    @Throws(InternalException::class)
    fun getTransactionInfo(txHash: String, chainId: Long): TransactionInfo
    @Throws(InternalException::class)
    fun getAllEvents(startBlockNumber: Long, endBlockNumber: Long, chainId: Long): List<Event>
}
