package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.persistence.model.Event
import com.ampnet.reportserviceeth.service.toLocalDateTime
import com.ampnet.reportserviceth.contract.IAssetCommon
import com.ampnet.reportserviceth.contract.TransactionEvents
import org.web3j.protocol.core.methods.response.TransactionReceipt
import java.math.BigInteger
import java.time.LocalDateTime

data class TransactionInfo(
    val type: TransactionType,
    val from: String,
    val to: String,
    val tokenValue: BigInteger,
    val tokenAmount: BigInteger?,
    val timestamp: LocalDateTime,
    val txHash: String,
    val asset: String,
    val assetTokenSymbol: String,
    val decimals: BigInteger,
    val stableCoinDecimals: BigInteger
) {
    /*
     * from - address of the wallet that reserved investment in the asset.
     * to - address of the CfManagerSoftcap contract.
     */
    constructor(
        event: TransactionEvents.InvestEventResponse,
        txRecipient: TransactionReceipt,
        asset: IAssetCommon.AssetCommonState,
        stableCoinDecimals: BigInteger
    ) : this(
        type = TransactionType.RESERVE_INVESTMENT,
        from = event.investor,
        to = txRecipient.to,
        tokenValue = event.tokenValue,
        tokenAmount = event.tokenAmount,
        timestamp = event.timestamp.toLocalDateTime(),
        txHash = txRecipient.transactionHash,
        asset = asset.name,
        assetTokenSymbol = asset.symbol,
        decimals = asset.decimals,
        stableCoinDecimals = stableCoinDecimals
    )

    /*
     * from - address of the wallet that canceled investment in the asset.
     * to - address of the CfManagerSoftcap contract.
     */
    constructor(
        event: TransactionEvents.CancelInvestmentEventResponse,
        txRecipient: TransactionReceipt,
        asset: IAssetCommon.AssetCommonState,
        stableCoinDecimals: BigInteger
    ) : this(
        type = TransactionType.CANCEL_INVESTMENT,
        from = event.investor,
        to = txRecipient.to,
        tokenValue = event.tokenValue,
        tokenAmount = event.tokenAmount,
        timestamp = event.timestamp.toLocalDateTime(),
        txHash = txRecipient.transactionHash,
        asset = asset.name,
        assetTokenSymbol = asset.symbol,
        decimals = asset.decimals,
        stableCoinDecimals = stableCoinDecimals
    )

    /*
     * from - address of the wallet that completed investment in the asset.
     * to - address of the CfManagerSoftcap contract.
     */
    constructor(
        event: TransactionEvents.ClaimEventResponse,
        txRecipient: TransactionReceipt,
        asset: IAssetCommon.AssetCommonState,
        stableCoinDecimals: BigInteger
    ) : this(
        type = TransactionType.COMPLETED_INVESTMENT,
        from = event.investor,
        to = txRecipient.to,
        tokenValue = event.tokenValue,
        tokenAmount = event.tokenAmount,
        timestamp = event.timestamp.toLocalDateTime(),
        txHash = txRecipient.transactionHash,
        asset = asset.name,
        assetTokenSymbol = asset.symbol,
        decimals = asset.decimals,
        stableCoinDecimals = stableCoinDecimals
    )

    /*
     * from - address of the PayoutManager contract.
     * to - address of the claiming investor.
     */
    constructor(
        event: TransactionEvents.PayoutClaimedEventResponse,
        txRecipient: TransactionReceipt,
        asset: IAssetCommon.AssetCommonState,
        stableCoinDecimals: BigInteger
    ) : this(
        type = TransactionType.REVENUE_SHARE,
        from = txRecipient.to,
        to = event.wallet,
        tokenValue = event.payoutAmount,
        tokenAmount = event.payoutAmount,
        timestamp = event.timestamp.toLocalDateTime(),
        txHash = txRecipient.transactionHash,
        asset = asset.name,
        assetTokenSymbol = asset.symbol,
        decimals = asset.decimals,
        stableCoinDecimals = stableCoinDecimals
    )

    constructor(event: Event) : this(
        type = event.type,
        from = event.fromAddress,
        to = event.toAddress,
        tokenValue = event.tokenValue,
        tokenAmount = event.tokenAmount,
        timestamp = event.timestamp.toLocalDateTime(),
        txHash = event.hash,
        asset = event.asset,
        assetTokenSymbol = event.tokenSymbol,
        decimals = BigInteger.valueOf(event.decimals.toLong()),
        stableCoinDecimals = BigInteger.valueOf(event.stableCoinDecimals.toLong())
    )
}
