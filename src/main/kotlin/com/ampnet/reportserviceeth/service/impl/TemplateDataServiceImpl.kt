package com.ampnet.reportserviceeth.service.impl

import com.ampnet.reportserviceeth.blockchain.BlockchainService
import com.ampnet.reportserviceeth.blockchain.TransactionInfo
import com.ampnet.reportserviceeth.config.ApplicationProperties
import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionsServiceRequest
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InvalidRequestException
import com.ampnet.reportserviceeth.exception.ResourceNotFoundException
import com.ampnet.reportserviceeth.grpc.userservice.UserService
import com.ampnet.reportserviceeth.service.EventService
import com.ampnet.reportserviceeth.service.IpfsService
import com.ampnet.reportserviceeth.service.TemplateDataService
import com.ampnet.reportserviceeth.service.TranslationService
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import com.ampnet.reportserviceeth.service.data.SingleTransactionSummary
import com.ampnet.reportserviceeth.service.data.Transaction
import com.ampnet.reportserviceeth.service.data.TransactionCancelInvestment
import com.ampnet.reportserviceeth.service.data.TransactionFactory
import com.ampnet.reportserviceeth.service.data.TransactionReserveInvestment
import com.ampnet.reportserviceeth.service.data.TransactionsSummary
import com.ampnet.reportserviceeth.service.data.Translations
import com.ampnet.reportserviceeth.service.data.UserInfo
import com.ampnet.reportserviceeth.service.data.UsersAccountsSummary
import mu.KLogging
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.streams.asSequence

@Service
class TemplateDataServiceImpl(
    private val blockchainService: BlockchainService,
    private val userService: UserService,
    private val translationService: TranslationService,
    private val eventService: EventService,
    private val ipfsService: IpfsService,
    private val applicationProperties: ApplicationProperties
) : TemplateDataService {

    companion object : KLogging()

    override fun getUserTransactionsData(request: TransactionsServiceRequest): TransactionsSummary {
        val transactions = eventService.getTransactions(request)
            .map { TransactionInfo(it) }
        val translations = translationService.getTranslations()
        val userInfo = UserInfo(userService.getUser(request.address))
        val transactionsWithNames = generateTransactionReportData(transactions, userInfo.language, translations)
        val logo = getLogoUrl(request.chainId, request.issuer)
        return TransactionsSummary(transactionsWithNames, userInfo, request.period, translations, logo)
    }

    override fun getUserTransactionData(request: TransactionServiceRequest): SingleTransactionSummary {
        val transaction = eventService.getTransaction(request)?.let { TransactionInfo(it) }
            ?: throw InvalidRequestException(ErrorCode.BLOCKCHAIN_TX_MISSING, "Transaction missing for: $request")
        val userWithInfo = UserInfo(userService.getUser(request.address))
        val translations = translationService.getTranslations(userWithInfo.language)
        val mappedTransaction =
            generateTransactionReportData(
                listOf(transaction), userWithInfo.language, translations
            ).firstOrNull()
                ?: throw InvalidRequestException(
                    ErrorCode.INT_UNSUPPORTED_TX, "Transaction with hash:${request.txHash} is not supported in report"
                )
        val logo = getLogoUrl(request.chainId, request.issuer)
        return SingleTransactionSummary(mappedTransaction, userWithInfo, translations, logo)
    }

    override fun getAllActiveUsersSummaryData(
        issuerRequest: IssuerRequest,
        periodRequest: PeriodServiceRequest
    ): UsersAccountsSummary {
        val users = userService.getUsersForIssuer(issuerRequest)
        val userTransactions = users.parallelStream().asSequence().associateBy(
            { it.address },
            {
                blockchainService.getTransactions(it.address, issuerRequest.chainId)
                    .filter { tx -> inTimePeriod(periodRequest, tx.timestamp) }
            }
        )
        if (userTransactions.values.all { it.isEmpty() })
            throw ResourceNotFoundException(
                ErrorCode.USER_MISSING_INFO,
                "None of the users went through KYC and completed at least one transaction."
            )

        val translations = translationService.getTranslations()
        val transactionsSummaryList = users.map { userResponse ->
            val transactions = getTransactions(userTransactions, userResponse.address)
            val userInfo = UserInfo(userResponse)
            TransactionsSummary(transactions, userInfo, periodRequest, translations)
        }
        val logo = getLogoUrl(issuerRequest.chainId, issuerRequest.address)
        return UsersAccountsSummary(transactionsSummaryList, logo)
    }

    private fun generateTransactionReportData(
        transactionsResponse: List<TransactionInfo>,
        language: String,
        translations: Translations
    ): List<Transaction> {
        val transactions = transactionsResponse.mapNotNull { TransactionFactory.createTransaction(it) }
        transactions.forEach { transaction ->
            transaction.setLanguage(language)
            transaction.translations = translations
            when (transaction) {
                is TransactionReserveInvestment -> {
//                    getExpectedProjectFunding(ownerUuidTo, projects)?.let {
//                        transaction.setPercentageInProject(it)
//                    }
                }
                is TransactionCancelInvestment -> {
//                    getExpectedProjectFunding(ownerUuidFrom, projects)?.let {
//                        transaction.setPercentageInProject(it)
//                    }
                }
            }
        }
        return transactions
    }

    private fun inTimePeriod(periodRequest: PeriodServiceRequest, dateTime: LocalDateTime): Boolean =
        periodRequest.to?.let { dateTime.isBefore(it) } ?: true &&
            periodRequest.from?.let { dateTime.isAfter(it) } ?: true

    private fun getTransactions(
        userTransactions: Map<String, List<TransactionInfo>>,
        userUuid: String
    ): List<Transaction> =
        userTransactions[userUuid]?.mapNotNull { TransactionFactory.createTransaction(it) }.orEmpty()

    @Suppress("ReturnCount")
    private fun getLogoUrl(chainId: Long, issuer: String): String? {
        val issuerState = blockchainService.getIssuerState(chainId, issuer) ?: return null
        val logoHash = ipfsService.getLogoHash(issuerState.info) ?: return null
        return applicationProperties.ipfs.ipfsUrl + logoHash
    }
}
