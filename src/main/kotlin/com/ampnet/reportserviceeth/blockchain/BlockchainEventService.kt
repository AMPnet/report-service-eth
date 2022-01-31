package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.persistence.model.Event
import com.ampnet.reportserviceeth.util.BlockNumber
import com.ampnet.reportserviceeth.util.ChainId

interface BlockchainEventService {
    fun getAllEvents(startBlockNumber: BlockNumber, endBlockNumber: BlockNumber, chainId: ChainId): List<Event>
}
