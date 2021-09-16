package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.persistence.model.Event

interface BlockchainEventService {
    fun getAllEvents(startBlockNumber: Long, endBlockNumber: Long, chainId: Long): List<Event>
}
