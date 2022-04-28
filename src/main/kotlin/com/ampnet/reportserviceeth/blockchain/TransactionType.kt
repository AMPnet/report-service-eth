package com.ampnet.reportserviceeth.blockchain

/* RESERVE_INVESTMENT - Refers to the investment placed into the campaign before it is finalized. Can be canceled.
   CANCEL_INVESTMENT - Refers to the cancellation of the reserved investments.
   COMPLETED_INVESTMENT - Refers to the investment claimed when the campaign is finalized.
   eligible for individual addresses which invested into the asset to collect it's share.
   REVENUE_SHARE - Refers to the investor addresses collecting revenue share.
 */
enum class TransactionType {
    RESERVE_INVESTMENT, CANCEL_INVESTMENT, TRANSFER_TOKEN, COMPLETED_INVESTMENT, REVENUE_SHARE
}
