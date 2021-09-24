package com.ampnet.reportserviceeth.persistence.model

import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceth.contract.IAssetCommon
import com.ampnet.reportserviceth.contract.TransactionEvents
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
    var issuer: String,

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
    var tokenSymbol: String,

    @Column(nullable = false)
    var blockNumber: Long,

    @Column(nullable = false)
    var blockHash: String,

    @Column(nullable = false)
    var timestamp: Long,

    @Column(nullable = false)
    var tokenValue: BigInteger,

    @Column(nullable = true)
    var tokenAmount: BigInteger?,

    @Column(nullable = true)
    var payoutId: Long?,

    @Column(nullable = true)
    var revenue: BigInteger?
) {
    /*
     * from - address of the wallet that reserved invested in the asset.
     * to - address of the CfManagerSoftcap contract
     */
    constructor(
        event: TransactionEvents.InvestEventResponse,
        chainId: Long,
        log: Log,
        asset: IAssetCommon.AssetCommonState
    ) : this(
        UUID.randomUUID(),
        chainId,
        event.investor,
        log.address,
        log.address,
        asset.issuer,
        log.transactionHash,
        TransactionType.RESERVE_INVESTMENT,
        log.logIndex.toLong(),
        asset.name,
        asset.symbol,
        log.blockNumber.toLong(),
        log.blockHash,
        event.timestamp.toLong(),
        event.tokenValue,
        event.tokenAmount,
        null,
        null
    )

    /*
     * from - address of the wallet that canceled investment in the asset.
     * to - address of the CfManagerSoftcap contract
     */
    constructor(
        event: TransactionEvents.CancelInvestmentEventResponse,
        chainId: Long,
        log: Log,
        asset: IAssetCommon.AssetCommonState
    ) : this(
        UUID.randomUUID(),
        chainId,
        event.investor,
        log.address,
        log.address,
        asset.issuer,
        log.transactionHash,
        TransactionType.CANCEL_INVESTMENT,
        log.logIndex.toLong(),
        asset.name,
        asset.symbol,
        log.blockNumber.toLong(),
        log.blockHash,
        event.timestamp.toLong(),
        event.tokenValue,
        event.tokenAmount,
        null,
        null
    )

    /*
     * from - address of the wallet that completed investment in the asset.
     * to - address of the CfManagerSoftcap contract
     */
    constructor(
        event: TransactionEvents.ClaimEventResponse,
        chainId: Long,
        log: Log,
        asset: IAssetCommon.AssetCommonState
    ) : this(
        UUID.randomUUID(),
        chainId,
        event.investor,
        log.address,
        log.address,
        asset.issuer,
        log.transactionHash,
        TransactionType.COMPLETED_INVESTMENT,
        log.logIndex.toLong(),
        asset.name,
        asset.symbol,
        log.blockNumber.toLong(),
        log.blockHash,
        event.timestamp.toLong(),
        event.tokenValue,
        event.tokenAmount,
        null,
        null
    )

    /*
     * from - address of the payout manager's wallet which created the PayoutManager contract.
     * to - address of the PayoutManager contract.
     */
    constructor(
        event: TransactionEvents.CreatePayoutEventResponse,
        chainId: Long,
        log: Log,
        asset: IAssetCommon.AssetCommonState
    ) : this(
        UUID.randomUUID(),
        chainId,
        event.creator,
        log.address,
        log.address,
        asset.issuer,
        log.transactionHash,
        TransactionType.CANCEL_INVESTMENT,
        log.logIndex.toLong(),
        asset.name,
        asset.symbol,
        log.blockNumber.toLong(),
        log.blockHash,
        event.timestamp.toLong(),
        event.amount,
        null,
        event.payoutId.toLong(),
        event.amount
    )

    /*
     * from - address of the PayoutManager contract.
     * to - address of the wallet which received revenue share payout.
     */
    constructor(
        event: TransactionEvents.ReleaseEventResponse,
        chainId: Long,
        log: Log,
        asset: IAssetCommon.AssetCommonState
    ) : this(
        UUID.randomUUID(),
        chainId,
        log.address,
        event.investor,
        log.address,
        asset.issuer,
        log.transactionHash,
        TransactionType.CANCEL_INVESTMENT,
        log.logIndex.toLong(),
        asset.name,
        asset.symbol,
        log.blockNumber.toLong(),
        log.blockHash,
        event.timestamp.toLong(),
        event.amount,
        null,
        event.payoutId.toLong(),
        event.amount
    )
}
