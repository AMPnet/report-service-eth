package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionServiceRequest

interface ReportingService {
    fun generatePdfReportForUserTransactions(
        address: String,
        chainId: Long,
        periodRequest: PeriodServiceRequest
    ): ByteArray

    fun generatePdfReportForUserTransaction(transactionServiceRequest: TransactionServiceRequest): ByteArray
    fun generatePdfReportForAllActiveUsers(
        address: String,
        chainId: Long,
        periodRequest: PeriodServiceRequest
    ): ByteArray
}
