package com.ampnet.reportserviceeth.controller

import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionServiceRequest
import com.ampnet.reportserviceeth.service.ReportingService
import mu.KLogging
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class ReportingController(private val reportingService: ReportingService) {

    companion object : KLogging()

    @GetMapping("/report/user/transactions")
    fun getUserTransactionsReport(
        @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) from: LocalDate?,
        @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) to: LocalDate?
    ): ResponseEntity<ByteArray> {
        val address = ControllerUtils.getAddressFromSecurityContext()
        logger.debug { "Received request to get report of transactions for address: $address" }
        val periodRequest = PeriodServiceRequest(from, to)
        val pdfContents = reportingService.generatePdfReportForUserTransactions(address, periodRequest)
        return ResponseEntity(pdfContents, ControllerUtils.getHttpHeadersForPdf(), HttpStatus.OK)
    }

    @GetMapping("/report/user/transaction")
    fun getUserTransactionReport(
        @RequestParam(name = "txHash") txHash: String
    ): ResponseEntity<ByteArray> {
        val address = ControllerUtils.getAddressFromSecurityContext()
        logger.debug {
            "Received request to get the report for a transaction: $txHash " +
                "for user address: $address"
        }
        val transactionServiceRequest = TransactionServiceRequest(address, txHash)
        val pdfContents = reportingService.generatePdfReportForUserTransaction(transactionServiceRequest)
        return ResponseEntity(pdfContents, ControllerUtils.getHttpHeadersForPdf(), HttpStatus.OK)
    }
}
