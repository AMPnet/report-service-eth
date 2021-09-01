package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.controller.pojo.TransactionServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionsServiceRequest
import com.ampnet.reportserviceeth.persistence.model.Event

interface EventService {
    fun getTransactions(request: TransactionsServiceRequest): List<Event>
    fun getTransaction(request: TransactionServiceRequest): Event?
}
