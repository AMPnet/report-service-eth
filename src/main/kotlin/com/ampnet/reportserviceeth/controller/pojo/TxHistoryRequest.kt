package com.ampnet.reportserviceeth.controller.pojo

data class TxHistoryRequest(val address: String, val chainId: Long, val period: PeriodServiceRequest)
