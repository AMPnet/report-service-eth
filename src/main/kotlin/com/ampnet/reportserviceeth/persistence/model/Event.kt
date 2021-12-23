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
    val decimals: Short,

    @Column(nullable = true)
    val tokenAmount: BigInteger?,

    @Column(nullable = true)
    val payoutId: Long?,

    @Column(nullable = true)
    val revenue: BigInteger?,

    @Column(nullable = false)
    val stableCoinDecimals: Short
) {
    /*
     * from - address of the wallet that reserved invested in the asset.
     * to - address of the CfManagerSoftcap contract
     */
    constructor(
        event: TransactionEvents.InvestEventResponse,
        chainId: Long,
        log: Log,
        asset: IAssetCommon.AssetCommonState,
        stableCoinDecimals: BigInteger
    ) : this(
        uuid = UUID.randomUUID(),
        chainId = chainId,
        fromAddress = event.investor,
        toAddress = log.address,
        contract = log.address,
        issuer = asset.issuer,
        hash = log.transactionHash,
        type = TransactionType.RESERVE_INVESTMENT,
        logIndex = log.logIndex.toLong(),
        asset = asset.name,
        tokenSymbol = asset.symbol,
        blockNumber = log.blockNumber.toLong(),
        blockHash = log.blockHash,
        timestamp = event.timestamp.toLong(),
        tokenValue = event.tokenValue,
        decimals = asset.decimals.toShort(),
        tokenAmount = event.tokenAmount,
        payoutId = null,
        revenue = null,
        stableCoinDecimals = stableCoinDecimals.toShort()
    )

    /*
     * from - address of the wallet that canceled investment in the asset.
     * to - address of the CfManagerSoftcap contract
     */
    constructor(
        event: TransactionEvents.CancelInvestmentEventResponse,
        chainId: Long,
        log: Log,
        asset: IAssetCommon.AssetCommonState,
        stableCoinDecimals: BigInteger
    ) : this(
        uuid = UUID.randomUUID(),
        chainId = chainId,
        fromAddress = event.investor,
        toAddress = log.address,
        contract = log.address,
        issuer = asset.issuer,
        hash = log.transactionHash,
        type = TransactionType.CANCEL_INVESTMENT,
        logIndex = log.logIndex.toLong(),
        asset = asset.name,
        tokenSymbol = asset.symbol,
        blockNumber = log.blockNumber.toLong(),
        blockHash = log.blockHash,
        timestamp = event.timestamp.toLong(),
        tokenValue = event.tokenValue,
        decimals = asset.decimals.toShort(),
        tokenAmount = event.tokenAmount,
        payoutId = null,
        revenue = null,
        stableCoinDecimals = stableCoinDecimals.toShort()
    )

    /*
     * from - address of the wallet that completed investment in the asset.
     * to - address of the CfManagerSoftcap contract
     */
    constructor(
        event: TransactionEvents.ClaimEventResponse,
        chainId: Long,
        log: Log,
        asset: IAssetCommon.AssetCommonState,
        stableCoinDecimals: BigInteger
    ) : this(
        uuid = UUID.randomUUID(),
        chainId = chainId,
        fromAddress = event.investor,
        toAddress = log.address,
        contract = log.address,
        issuer = asset.issuer,
        hash = log.transactionHash,
        type = TransactionType.COMPLETED_INVESTMENT,
        logIndex = log.logIndex.toLong(),
        asset = asset.name,
        tokenSymbol = asset.symbol,
        blockNumber = log.blockNumber.toLong(),
        blockHash = log.blockHash,
        timestamp = event.timestamp.toLong(),
        tokenValue = event.tokenValue,
        decimals = asset.decimals.toShort(),
        tokenAmount = event.tokenAmount,
        payoutId = null,
        revenue = null,
        stableCoinDecimals = stableCoinDecimals.toShort()
    )

    /*
     * from - address of the payout manager's wallet which created the PayoutManager contract.
     * to - address of the PayoutManager contract.
     */
    constructor(
        event: TransactionEvents.CreatePayoutEventResponse,
        chainId: Long,
        log: Log,
        asset: IAssetCommon.AssetCommonState,
        stableCoinDecimals: BigInteger
    ) : this(
        uuid = UUID.randomUUID(),
        chainId = chainId,
        fromAddress = event.creator,
        toAddress = log.address,
        contract = log.address,
        issuer = asset.issuer,
        hash = log.transactionHash,
        type = TransactionType.CANCEL_INVESTMENT,
        logIndex = log.logIndex.toLong(),
        asset = asset.name,
        tokenSymbol = asset.symbol,
        blockNumber = log.blockNumber.toLong(),
        blockHash = log.blockHash,
        timestamp = event.timestamp.toLong(),
        tokenValue = event.amount,
        decimals = asset.decimals.toShort(),
        tokenAmount = null,
        payoutId = event.payoutId.toLong(),
        revenue = event.amount,
        stableCoinDecimals = stableCoinDecimals.toShort()
    )

    /*
     * from - address of the PayoutManager contract.
     * to - address of the wallet which received revenue share payout.
     */
    constructor(
        event: TransactionEvents.ReleaseEventResponse,
        chainId: Long,
        log: Log,
        asset: IAssetCommon.AssetCommonState,
        stableCoinDecimals: BigInteger
    ) : this(
        uuid = UUID.randomUUID(),
        chainId = chainId,
        fromAddress = log.address,
        toAddress = event.investor,
        contract = log.address,
        issuer = asset.issuer,
        hash = log.transactionHash,
        type = TransactionType.CANCEL_INVESTMENT,
        logIndex = log.logIndex.toLong(),
        asset = asset.name,
        tokenSymbol = asset.symbol,
        blockNumber = log.blockNumber.toLong(),
        blockHash = log.blockHash,
        timestamp = event.timestamp.toLong(),
        tokenValue = event.amount,
        decimals = asset.decimals.toShort(),
        tokenAmount = null,
        payoutId = event.payoutId.toLong(),
        revenue = event.amount,
        stableCoinDecimals = stableCoinDecimals.toShort()
    )
}
