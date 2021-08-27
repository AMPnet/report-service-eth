package com.ampnet.reportserviceeth.persistence.model

import com.ampnet.reportserviceeth.blockchain.TransactionType
import org.web3j.protocol.core.methods.response.Log
import java.math.BigInteger
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "event")
@Suppress("LongParameterList")
class Event(
    @Id
    val uuid: UUID,

    @Column(nullable = false)
    var chainId: Long,

    @Column(nullable = false)
    var fromAddress: String,

    @Column(nullable = false)
    var toAddress: String,

    @Column(nullable = false)
    var contract: String,

    @Column(nullable = false)
    var hash: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: TransactionType,

    @Column(nullable = false)
    var logIndex: Long,

    @Column(nullable = false)
    var asset: String,

    @Column(nullable = false)
    var blockNumber: Long,

    @Column(nullable = false)
    var blockHash: String,

    @Column(nullable = false)
    var timestamp: Long,

    @Column(nullable = true)
    var tokenAmount: BigInteger?,

    @Column(nullable = true)
    var tokenValue: BigInteger?,

    @Column(nullable = true)
    var payoutId: Long?,

    @Column(nullable = true)
    var revenue: BigInteger?
) {
    constructor(
        event: TransactionEvents.InvestEventResponse,
        chainId: Long,
        log: Log,
        asset: String
    ) : this(
        UUID.randomUUID(),
        chainId,
        event.investor,
        log.address,
        log.address,
        log.transactionHash,
        TransactionType.RESERVE_INVESTMENT,
        log.logIndex.toLong(),
        asset,
        log.blockNumber.toLong(),
        log.blockHash,
        event.timestamp.toLong(),
        event.tokenAmount,
        event.tokenValue,
        null,
        null
    )
    constructor(
        event: TransactionEvents.CancelInvestmentEventResponse,
        chainId: Long,
        log: Log,
        asset: String
    ) : this(
        UUID.randomUUID(),
        chainId,
        event.investor,
        log.address,
        log.address,
        log.transactionHash,
        TransactionType.CANCEL_INVESTMENT,
        log.logIndex.toLong(),
        asset,
        log.blockNumber.toLong(),
        log.blockHash,
        event.timestamp.toLong(),
        event.tokenAmount,
        event.tokenValue,
        null,
        null
    )
    constructor(
        event: TransactionEvents.ClaimEventResponse,
        chainId: Long,
        log: Log,
        asset: String
    ) : this(
        UUID.randomUUID(),
        chainId,
        event.investor,
        log.address,
        log.address,
        log.transactionHash,
        TransactionType.COMPLETED_INVESTMENT,
        log.logIndex.toLong(),
        asset,
        log.blockNumber.toLong(),
        log.blockHash,
        event.timestamp.toLong(),
        event.tokenAmount,
        event.tokenValue,
        null,
        null
    )
    constructor(
        event: TransactionEvents.CreatePayoutEventResponse,
        chainId: Long,
        log: Log,
        asset: String
    ) : this(
        UUID.randomUUID(),
        chainId,
        event.creator,
        log.address,
        log.address,
        log.transactionHash,
        TransactionType.CANCEL_INVESTMENT,
        log.logIndex.toLong(),
        asset,
        log.blockNumber.toLong(),
        log.blockHash,
        event.timestamp.toLong(),
        null,
        event.amount,
        event.payoutId.toLong(),
        event.amount
    )
}
