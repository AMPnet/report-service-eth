package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionServiceRequest
import com.ampnet.reportserviceeth.service.data.SingleTransactionSummary
import com.ampnet.reportserviceeth.service.data.TransactionsSummary
import com.ampnet.reportserviceeth.service.data.UsersAccountsSummary

interface TemplateDataService {
    fun getUserTransactionsData(address: String, periodRequest: PeriodServiceRequest): TransactionsSummary
    fun getUserTransactionData(request: TransactionServiceRequest): SingleTransactionSummary
    fun getAllActiveUsersSummaryData(issuer: String, periodRequest: PeriodServiceRequest): UsersAccountsSummary
}
