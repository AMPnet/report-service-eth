package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionServiceRequest
import com.ampnet.reportserviceeth.service.data.IssuerRequest

interface TemplateService {
    fun generateTemplateForUserTransactions(address: String, chainId: Long, periodRequest: PeriodServiceRequest): String
    fun generateTemplateForUserTransaction(transactionServiceRequest: TransactionServiceRequest): String
    fun generateTemplateForAllActiveUsers(issuerRequest: IssuerRequest, periodRequest: PeriodServiceRequest): String
}
