package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionsServiceRequest

interface ReportingService {
    fun generatePdfReportForUserTransactions(
        request: TransactionsServiceRequest
    ): ByteArray

    fun generatePdfReportForUserTransaction(transactionServiceRequest: TransactionServiceRequest): ByteArray
    fun generatePdfReportForAllActiveUsers(
        address: String,
        chainId: Long,
        periodRequest: PeriodServiceRequest
    ): ByteArray
}
