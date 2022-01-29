package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionsServiceRequest
import com.ampnet.reportserviceeth.util.ChainId
import com.ampnet.reportserviceeth.util.ContractAddress

interface ReportingService {
    fun generatePdfReportForUserTransactions(request: TransactionsServiceRequest): ByteArray
    fun generatePdfReportForUserTransaction(transactionServiceRequest: TransactionServiceRequest): ByteArray
    fun generatePdfReportForAllActiveUsers(
        address: ContractAddress,
        chainId: ChainId,
        periodRequest: PeriodServiceRequest
    ): ByteArray
}
