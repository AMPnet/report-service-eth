package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionsServiceRequest
import com.ampnet.reportserviceeth.service.data.IssuerRequest

interface TemplateService {
    fun generateTemplateForUserTransactions(request: TransactionsServiceRequest): String
    fun generateTemplateForUserTransaction(transactionServiceRequest: TransactionServiceRequest): String
    fun generateTemplateForAllActiveUsers(issuerRequest: IssuerRequest, periodRequest: PeriodServiceRequest): String
}
