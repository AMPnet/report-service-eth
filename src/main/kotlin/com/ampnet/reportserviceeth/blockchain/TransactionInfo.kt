package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.persistence.model.Event
import com.ampnet.reportserviceeth.service.data.EventServiceResponse
import com.ampnet.reportserviceeth.service.toLocalDateTime
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
    val asset: String?,
    val assetTokenSymbol: String?
) {
    /*
     * from - address of the wallet that reserved investment in the asset.
     * to - address of the CfManagerSoftcap contract.
     */
    constructor(
        event: TransactionEvents.InvestEventResponse,
        txRecipient: TransactionReceipt,
        asset: IAsset.AssetState?
    ) : this(
        TransactionType.RESERVE_INVESTMENT,
        event.investor,
        txRecipient.to,
        event.tokenValue,
        event.tokenAmount,
        event.timestamp.toLocalDateTime(),
        txRecipient.transactionHash,
        asset?.name,
        asset?.symbol
    )

    /*
     * from - address of the wallet that canceled investment in the asset.
     * to - address of the CfManagerSoftcap contract.
     */
    constructor(
        event: TransactionEvents.CancelInvestmentEventResponse,
        txRecipient: TransactionReceipt,
        asset: IAsset.AssetState?
    ) : this(
        TransactionType.CANCEL_INVESTMENT,
        event.investor,
        txRecipient.to,
        event.tokenValue,
        event.tokenAmount,
        event.timestamp.toLocalDateTime(),
        txRecipient.transactionHash,
        asset?.name,
        asset?.symbol
    )

    /*
     * from - address of the wallet that completed investment in the asset.
     * to - address of the CfManagerSoftcap contract.
     */
    constructor(
        event: TransactionEvents.ClaimEventResponse,
        txRecipient: TransactionReceipt,
        asset: IAsset.AssetState?
    ) : this(
        TransactionType.COMPLETED_INVESTMENT,
        event.investor,
        txRecipient.to,
        event.tokenValue,
        event.tokenAmount,
        event.timestamp.toLocalDateTime(),
        txRecipient.transactionHash,
        asset?.name,
        asset?.symbol
    )

    /*
     * from - address of the payout manager's wallet which created the PayoutManager contract.
     * to - address of the PayoutManager contract.
     */
    constructor(
        event: TransactionEvents.CreatePayoutEventResponse,
        txRecipient: TransactionReceipt,
        asset: IAsset.AssetState?
    ) : this(
        TransactionType.REVENUE_SHARE,
        event.creator,
        txRecipient.to,
        event.amount,
        null,
        event.timestamp.toLocalDateTime(),
        txRecipient.transactionHash,
        asset?.name,
        asset?.symbol
    )

    constructor(event: Event) : this (
        event.type,
        event.fromAddress,
        event.toAddress,
        event.tokenValue,
        event.tokenAmount,
        event.timestamp.toLocalDateTime(),
        event.hash,
        event.asset,
        null
    )
}
