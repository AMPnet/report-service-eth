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
    val chainId: Long,

    @Column(nullable = false)
    val fromAddress: String,

    @Column(nullable = false)
    val toAddress: String,

    @Column(nullable = false)
    val contract: String,

    @Column(nullable = false)
    val issuer: String,

    @Column(nullable = false)
    val hash: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: TransactionType,

    @Column(nullable = false)
    val logIndex: Long,

    @Column(nullable = false)
    val asset: String,

    @Column(nullable = false)
    val tokenSymbol: String,

    @Column(nullable = false)
    val blockNumber: Long,

    @Column(nullable = false)
    val blockHash: String,

    @Column(nullable = false)
    val timestamp: Long,

    @Column(nullable = false)
    val tokenValue: BigInteger,

    @Column(nullable = false)
    val decimals: BigInteger,

    @Column(nullable = true)
    val tokenAmount: BigInteger?,

    @Column(nullable = true)
    val payoutId: Long?,

    @Column(nullable = true)
    val revenue: BigInteger?
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
        asset.decimals,
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
        asset.decimals,
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
        asset.decimals,
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
        asset.decimals,
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
        asset.decimals,
        null,
        event.payoutId.toLong(),
        event.amount
    )
}
