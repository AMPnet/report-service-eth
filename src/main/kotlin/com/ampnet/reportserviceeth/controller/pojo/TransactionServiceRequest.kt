package com.ampnet.reportserviceeth.controller.pojo

data class TransactionServiceRequest(
    val address: String,
    val txHash: String,
    val chainId: Long
)
