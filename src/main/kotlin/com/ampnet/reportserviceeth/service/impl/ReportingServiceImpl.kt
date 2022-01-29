package com.ampnet.reportserviceeth.service.impl

import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionsServiceRequest
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.service.ReportingService
import com.ampnet.reportserviceeth.service.TemplateService
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import com.ampnet.reportserviceeth.util.ChainId
import com.ampnet.reportserviceeth.util.ContractAddress
import com.ampnet.reportserviceeth.util.WalletAddress
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import mu.KLogging
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL

@Service
class ReportingServiceImpl(
    private val templateService: TemplateService,
    private val rendererBuilder: PdfRendererBuilder
) : ReportingService {

    companion object : KLogging()

    private val rootTemplate = "templates/root.htm"

    override fun generatePdfReportForUserTransactions(request: TransactionsServiceRequest): ByteArray {
        val template = templateService.generateTemplateForUserTransactions(request)
        return generateFromTemplateToByteArray(template)
    }

    override fun generatePdfReportForUserTransaction(transactionServiceRequest: TransactionServiceRequest): ByteArray {
        val template = templateService.generateTemplateForUserTransaction(transactionServiceRequest)
        return generateFromTemplateToByteArray(template)
    }

    override fun generatePdfReportForAllActiveUsers(
        address: ContractAddress,
        chainId: ChainId,
        periodRequest: PeriodServiceRequest
    ): ByteArray {
        val template = templateService.generateTemplateForAllActiveUsers(IssuerRequest(address, chainId), periodRequest)
        return generateFromTemplateToByteArray(template)
    }

    private fun generateFromTemplateToByteArray(html: String): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val baseUri = getRootTemplateUri()
        try {
            rendererBuilder.withHtmlContent(html, baseUri.toString())
            rendererBuilder.toStream(outputStream)
            rendererBuilder.run()
            return outputStream.toByteArray()
        } catch (ex: IOException) {
            logger.warn { ex.message }
            throw InternalException(
                ErrorCode.INT_GENERATING_PDF,
                "Could not generate pdf with PdfRendererBuilder.",
                ex
            )
        }
    }

    private fun getRootTemplateUri(): URL {
        return javaClass.classLoader.getResource("templates/root.htm")
            ?: throw InternalException(
                ErrorCode.INT_GENERATING_PDF,
                "Could not find local resources under: $rootTemplate"
            )
    }
}
