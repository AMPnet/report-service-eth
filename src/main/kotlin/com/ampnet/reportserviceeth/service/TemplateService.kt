package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionServiceRequest

interface TemplateService {
    fun generateTemplateForUserTransactions(address: String, periodRequest: PeriodServiceRequest): String
    fun generateTemplateForUserTransaction(transactionServiceRequest: TransactionServiceRequest): String
    fun generateTemplateForAllActiveUsers(address: String, periodRequest: PeriodServiceRequest): String
}
