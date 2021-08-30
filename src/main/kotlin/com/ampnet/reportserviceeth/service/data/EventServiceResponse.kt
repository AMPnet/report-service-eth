package com.ampnet.reportserviceeth.service.data

import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.persistence.model.Event
import java.math.BigInteger

data class EventServiceResponse(
    val fromAddress: String,
    val toAddress: String,
    val chainId: Long,
    val hash: String,
    val type: TransactionType,
    val asset: String,
    val timestamp: Long,
    val tokenValue: BigInteger,
    val tokenAmount: BigInteger?,
    val payoutId: Long?,
    val revenue: BigInteger?
) {
    constructor(event: Event) : this(
        event.fromAddress,
        event.toAddress,
        event.chainId,
        event.hash,
        event.type,
        event.asset,
        event.timestamp,
        event.tokenValue,
        event.tokenAmount,
        event.payoutId,
        event.revenue
    )
}
