package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.controller.pojo.TransactionServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionsServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TxHistoryRequest
import com.ampnet.reportserviceeth.persistence.model.Event
import com.ampnet.reportserviceeth.service.data.EventServiceResponse

interface EventService {
    fun getTransactions(request: TxHistoryRequest): List<EventServiceResponse>
    fun getTransactionsForIssuer(request: TransactionsServiceRequest): List<Event>
    fun getTransaction(request: TransactionServiceRequest): Event?
}
