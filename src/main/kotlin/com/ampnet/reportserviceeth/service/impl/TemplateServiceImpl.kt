package com.ampnet.reportserviceeth.service.impl

import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionServiceRequest
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.service.TemplateDataService
import com.ampnet.reportserviceeth.service.TemplateService
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

    override fun generateTemplateForUserTransactions(address: String, periodRequest: PeriodServiceRequest): String {
        val transactions = templateDataService.getUserTransactionsData(address, periodRequest)
        return processThymeleafTemplate(transactions, userTransactionsTemplate)
    }

    override fun generateTemplateForUserTransaction(transactionServiceRequest: TransactionServiceRequest): String {
        val transaction = templateDataService.getUserTransactionData(transactionServiceRequest)
        return processThymeleafTemplate(transaction, userTransactionTemplate)
    }

    override fun generateTemplateForAllActiveUsers(address: String, periodRequest: PeriodServiceRequest): String {
        val activeUsersSummaryData = templateDataService.getAllActiveUsersSummaryData(address, periodRequest)
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
