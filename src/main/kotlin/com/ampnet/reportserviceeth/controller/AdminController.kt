package com.ampnet.reportserviceeth.controller

import com.ampnet.reportserviceeth.blockchain.BlockchainService
import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InvalidRequestException
import com.ampnet.reportserviceeth.service.ReportingService
import com.ampnet.reportserviceeth.service.XlsxService
import com.ampnet.reportserviceeth.service.data.IssuerCampaignRequest
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import com.ampnet.reportserviceeth.util.ChainId
import com.ampnet.reportserviceeth.util.ContractAddress
import com.ampnet.reportserviceeth.util.WalletAddress
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

    @GetMapping("/admin/{chainId}/{issuer}/report/user")
    fun getActiveUsersReport(
        @PathVariable("chainId") rawChainId: Long,
        @PathVariable issuer: String,
        @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) from: LocalDate?,
        @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) to: LocalDate?
    ): ResponseEntity<ByteArray> {
        val user = ControllerUtils.getAddressFromSecurityContext()
        logger.info {
            "Received request to get users accounts summary for all the active users for issuer: $issuer"
        }
        val issuerAddress = ContractAddress(issuer)
        val chainId = ChainId(rawChainId)
        verifyUserIsIssuerOwner(user, issuerAddress, chainId)
        val periodRequest = PeriodServiceRequest(from, to)
        val pdfContents = reportingService.generatePdfReportForAllActiveUsers(issuerAddress, chainId, periodRequest)
        return ResponseEntity(pdfContents, ControllerUtils.getHttpHeadersForPdf(), HttpStatus.OK)
    }

    @GetMapping("/admin/{chainId}/{issuer}/report/xlsx")
    fun getXlsxReport(
        @PathVariable("chainId") rawChainId: Long,
        @PathVariable issuer: String
    ): ResponseEntity<ByteArray> {
        val user = ControllerUtils.getAddressFromSecurityContext()
        logger.info { "Received request to get users xlsx report for issuer: $issuer" }
        val issuerAddress = ContractAddress(issuer)
        val chainId = ChainId(rawChainId)
        verifyUserIsIssuerOwner(user, issuerAddress, chainId)
        val xlsxReport = xlsxService.generateXlsx(IssuerRequest(issuerAddress, chainId))
        logger.info { "Successfully generated xlsx report for issuer: $issuer" }
        return createXlsxResponseEntity(xlsxReport)
    }

    @GetMapping("/admin/{chainId}/{issuer}/{campaign}/report/xlsx")
    fun getXlsxReportForCampaign(
        @PathVariable("chainId") rawChainId: Long,
        @PathVariable issuer: String,
        @PathVariable campaign: String
    ): ResponseEntity<ByteArray> {
        val user = ControllerUtils.getAddressFromSecurityContext()
        logger.info { "Received request to get users xlsx report for issuer: $issuer and campaign: $campaign" }
        val issuerAddress = ContractAddress(issuer)
        val campaignAddress = ContractAddress(campaign)
        val chainId = ChainId(rawChainId)
        verifyUserIsIssuerOwner(user, issuerAddress, chainId)
        val xlsxReport = xlsxService.generateXlsx(IssuerCampaignRequest(issuerAddress, campaignAddress, chainId))
        logger.info { "Successfully generated xlsx report for issuer: $issuer and campaign: $campaign" }
        return createXlsxResponseEntity(xlsxReport)
    }

    private fun verifyUserIsIssuerOwner(address: WalletAddress, issuer: ContractAddress, chainId: ChainId) {
        val issuerOwner = blockchainService.getIssuerOwner(IssuerRequest(issuer, chainId))
        if (address != issuerOwner)
            throw InvalidRequestException(ErrorCode.USER_NOT_ISSUER, "Issuer owner is address: $issuerOwner")
    }

    private fun createXlsxResponseEntity(xlsxReport: ByteArray): ResponseEntity<ByteArray> {
        val httpHeaders = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_OCTET_STREAM
        }
        return ResponseEntity(xlsxReport, httpHeaders, HttpStatus.OK)
    }
}
