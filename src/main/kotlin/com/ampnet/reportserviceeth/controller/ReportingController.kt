package com.ampnet.reportserviceeth.controller

import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionServiceRequest
import com.ampnet.reportserviceeth.service.ReportingService
import mu.KLogging
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class ReportingController(private val reportingService: ReportingService) {

    companion object : KLogging()

    @GetMapping("/report/{chainId}/user/transactions")
    fun getUserTransactionsReport(
        @PathVariable chainId: Long,
        @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) from: LocalDate?,
        @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) to: LocalDate?
    ): ResponseEntity<ByteArray> {
        val address = ControllerUtils.getAddressFromSecurityContext()
        logger.debug { "Received request to get report of transactions for address: $address" }
        val periodRequest = PeriodServiceRequest(from, to)
        val pdfContents = reportingService.generatePdfReportForUserTransactions(address, chainId, periodRequest)
        return ResponseEntity(pdfContents, ControllerUtils.getHttpHeadersForPdf(), HttpStatus.OK)
    }

    @GetMapping("/report/{chainId}/user/transaction")
    fun getUserTransactionReport(
        @PathVariable chainId: Long,
        @RequestParam(name = "txHash") txHash: String
    ): ResponseEntity<ByteArray> {
        val address = ControllerUtils.getAddressFromSecurityContext()
        logger.debug {
            "Received request to get the report for a transaction: $txHash " +
                "for user address: $address"
        }
        val transactionServiceRequest = TransactionServiceRequest(address, txHash, chainId)
        val pdfContents = reportingService.generatePdfReportForUserTransaction(transactionServiceRequest)
        return ResponseEntity(pdfContents, ControllerUtils.getHttpHeadersForPdf(), HttpStatus.OK)
    }
}
