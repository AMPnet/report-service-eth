package com.ampnet.reportserviceeth.controller.pojo

data class TransactionsServiceRequest(
    val address: String,
    val chainId: Long,
    val issuer: String,
    val period: PeriodServiceRequest
)
