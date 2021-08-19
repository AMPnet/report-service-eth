package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.controller.pojo.TxHistoryRequest
import com.ampnet.reportserviceeth.service.data.EventServiceResponse

interface EventService {
    fun getTransactions(request: TxHistoryRequest): List<EventServiceResponse>
}
