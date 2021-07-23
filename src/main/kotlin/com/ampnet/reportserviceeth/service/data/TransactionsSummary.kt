package com.ampnet.reportserviceeth.service.data

import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import mu.KLogging
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

const val DATE_FORMAT = "MMM dd, yyyy"
const val DEFAULT_LOGO = "https://ampnet.io/assets/images/logo-amp.png"

class TransactionsSummary(
    val transactions: List<Transaction>,
    val userInfo: UserInfo,
    val periodRequest: PeriodServiceRequest,
    val translations: Translations,
    val logo: String = DEFAULT_LOGO
) {
    companion object : KLogging()

    private val transactionsByType = transactions.groupBy { it.type }
    private val locale: Locale = if (userInfo.language.isBlank()) {
        Locale.ENGLISH
    } else {
        Locale.forLanguageTag(userInfo.language)
    }
    val period: String = getPeriod(periodRequest)
    val dateOfFinish: String? = getDateOfFinish(transactions, periodRequest)
    val revenueShare = sumTransactionAmountsByType(TransactionType.CLAIM_TOKENS).toEurAmount()
    val investments = (
        sumTransactionAmountsByType(TransactionType.INVEST) -
            sumTransactionAmountsByType(TransactionType.CANCEL_INVESTMENT)
        ).toEurAmount()

    private fun getPeriod(periodRequest: PeriodServiceRequest): String {
        val fromPeriod = formatToYearMonthDay(periodRequest.from ?: userInfo.createdAt)
        val toPeriod = formatToYearMonthDay(periodRequest.to ?: LocalDateTime.now())
        return "$fromPeriod - $toPeriod"
    }

    private fun getDateOfFinish(transactions: List<Transaction>, periodRequest: PeriodServiceRequest): String? {
        return if (transactions.isEmpty()) {
            formatToYearMonthDay(periodRequest.to ?: LocalDateTime.now())
        } else {
            formatToYearMonthDay(periodRequest.to) ?: formatToYearMonthDay(transactions.last().date)
        }
    }

    private fun formatToYearMonthDay(date: LocalDateTime?): String? =
        date?.format(DateTimeFormatter.ofPattern(DATE_FORMAT).withLocale(locale))

    private fun sumTransactionAmountsByType(type: TransactionType): BigInteger {
        return transactionsByType[type]?.sumOf { it.amount } ?: BigInteger.ZERO
    }
}
