package com.ampnet.reportserviceeth.controller.pojo

import com.ampnet.reportserviceeth.service.data.EventServiceResponse

data class TxHistoryResponse(val transactions: List<EventServiceResponse>)
