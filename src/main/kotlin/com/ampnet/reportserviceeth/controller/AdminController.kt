package com.ampnet.reportserviceeth.controller

import com.ampnet.reportserviceeth.blockchain.BlockchainService
import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InvalidRequestException
import com.ampnet.reportserviceeth.service.ReportingService
import com.ampnet.reportserviceeth.service.XlsxService
import mu.KLogging
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class AdminController(
    private val reportingService: ReportingService,
    private val xlsxService: XlsxService,
    private val blockchainService: BlockchainService
) {

    companion object : KLogging()

    @GetMapping("/admin/{issuer}/report/user")
    fun getActiveUsersReport(
        @PathVariable issuer: String,
        @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) from: LocalDate?,
        @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) to: LocalDate?
    ): ResponseEntity<ByteArray> {
        val user = ControllerUtils.getAddressFromSecurityContext()
        logger.info {
            "Received request to get users accounts summary for all the active users for issuer: $issuer"
        }
        verifyUserIsIssuerOwner(user, issuer)
        val periodRequest = PeriodServiceRequest(from, to)
        val pdfContents = reportingService.generatePdfReportForAllActiveUsers(issuer, periodRequest)
        return ResponseEntity(pdfContents, ControllerUtils.getHttpHeadersForPdf(), HttpStatus.OK)
    }

    @GetMapping("/admin/{issuer}/report/xlsx")
    fun getXlsxReport(
        @PathVariable issuer: String
    ): ResponseEntity<ByteArray> {
        val user = ControllerUtils.getAddressFromSecurityContext()
        logger.info { "Received request to get users xlsx report for issuer: $issuer" }
        verifyUserIsIssuerOwner(user, issuer)
        val pdfContents = xlsxService.generateXlsx(issuer)
        val httpHeaders = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_OCTET_STREAM
        }
        logger.info { "Successfully generate xlsx report" }
        return ResponseEntity(pdfContents, httpHeaders, HttpStatus.OK)
    }

    private fun verifyUserIsIssuerOwner(address: String, issuer: String) {
        val issuerOwner = blockchainService.getIssuerOwner(issuer)
        if (address.lowercase() != issuerOwner.lowercase())
            throw InvalidRequestException(ErrorCode.USER_NOT_ISSUER, "Issuer owner is address: $issuerOwner")
    }
}
