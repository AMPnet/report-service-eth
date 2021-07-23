package com.ampnet.reportserviceeth.blockchain

import java.math.BigInteger
import java.time.LocalDateTime

data class TransactionInfo(
    val type: TransactionType,
    val from: String,
    val to: String,
    val amount: BigInteger,
    val tokenAmount: BigInteger,
    val timestamp: LocalDateTime,
    val txHash: String,
    val asset: String?
)
