package com.ampnet.reportserviceeth.service.impl

import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionsServiceRequest
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.service.TemplateDataService
import com.ampnet.reportserviceeth.service.TemplateService
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import mu.KLogging
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.exceptions.TemplateEngineException

@Service
class TemplateServiceImpl(
    val templateDataService: TemplateDataService,
    val templateEngine: TemplateEngine
) : TemplateService {

    companion object : KLogging()

    internal val userTransactionsTemplate = "user-transactions-template"
    internal val userTransactionTemplate = "user-transaction-template"
    internal val usersAccountsSummaryTemplate = "users-accounts-summary-template"

    override fun generateTemplateForUserTransactions(request: TransactionsServiceRequest): String {
        val transactions = templateDataService.getUserTransactionsData(request)
        return processThymeleafTemplate(transactions, userTransactionsTemplate)
    }

    override fun generateTemplateForUserTransaction(transactionServiceRequest: TransactionServiceRequest): String {
        val transaction = templateDataService.getUserTransactionData(transactionServiceRequest)
        return processThymeleafTemplate(transaction, userTransactionTemplate)
    }

    override fun generateTemplateForAllActiveUsers(
        issuerRequest: IssuerRequest,
        periodRequest: PeriodServiceRequest
    ): String {
        val activeUsersSummaryData = templateDataService.getAllActiveUsersSummaryData(issuerRequest, periodRequest)
        return processThymeleafTemplate(activeUsersSummaryData, usersAccountsSummaryTemplate)
    }

    private fun processThymeleafTemplate(data: Any, templateName: String): String {
        val context = Context()
        context.setVariable("data", data)
        try {
            return templateEngine.process(templateName, context)
        } catch (ex: TemplateEngineException) {
            logger.warn { ex.message }
            throw InternalException(
                ErrorCode.INT_GENERATING_PDF,
                "Could not process $templateName with thymeleaf.",
                ex
            )
        }
    }
}
