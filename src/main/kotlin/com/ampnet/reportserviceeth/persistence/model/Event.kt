package com.ampnet.reportserviceeth.persistence.model

import com.ampnet.reportserviceeth.blockchain.TransactionType
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
)
