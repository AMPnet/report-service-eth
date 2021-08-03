package com.ampnet.reportserviceeth.blockchain

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
    val asset: String?
) {
    constructor(
        event: TransactionEvents.InvestEventResponse,
        txRecipient: TransactionReceipt,
        asset: String?
    ) : this(
        TransactionType.RESERVE_INVESTMENT,
        event.investor,
        txRecipient.to,
        event.tokenValue,
        event.tokenAmount,
        event.timestamp.toLocalDateTime(),
        txRecipient.transactionHash,
        asset
    )

    constructor(
        event: TransactionEvents.CancelInvestmentEventResponse,
        txRecipient: TransactionReceipt,
        asset: String?
    ) : this(
        TransactionType.CANCEL_INVESTMENT,
        event.investor,
        txRecipient.to,
        event.tokenValue,
        event.tokenAmount,
        event.timestamp.toLocalDateTime(),
        txRecipient.transactionHash,
        asset
    )

    constructor(
        event: TransactionEvents.ClaimEventResponse,
        txRecipient: TransactionReceipt,
        asset: String?
    ) : this(
        TransactionType.COMPLETED_INVESTMENT,
        event.investor,
        txRecipient.to,
        event.tokenValue,
        event.tokenAmount,
        event.timestamp.toLocalDateTime(),
        txRecipient.transactionHash,
        asset
    )

    constructor(
        event: TransactionEvents.CreatePayoutEventResponse,
        txRecipient: TransactionReceipt,
        asset: String?
    ) : this(
        TransactionType.REVENUE_SHARE,
        event.creator,
        txRecipient.to,
        event.amount,
        null,
        event.timestamp.toLocalDateTime(),
        txRecipient.transactionHash,
        asset
    )
}
