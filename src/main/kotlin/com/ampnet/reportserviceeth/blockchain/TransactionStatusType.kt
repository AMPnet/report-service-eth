package com.ampnet.reportserviceeth.blockchain

/**
 * PAID_IN status type refers to: [TransactionType.REVENUE_SHARE], [TransactionType.CANCEL_INVESTMENT]
 * PAID_OUT status type refers to: [TransactionType.RESERVE_INVESTMENT], [TransactionType.COMPLETED_INVESTMENT]
 */
enum class TransactionStatusType {
    PAID_IN,
    PAID_OUT
}
