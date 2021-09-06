package com.ampnet.reportserviceeth.service.data

import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.service.toEther
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
    val dateOfFinish: String = formatToYearMonthDay(periodRequest.to)
    val revenueShare = sumTransactionAmountsByType(TransactionType.REVENUE_SHARE).toEther()
    val investments = (
        sumTransactionAmountsByType(TransactionType.RESERVE_INVESTMENT) -
            sumTransactionAmountsByType(TransactionType.CANCEL_INVESTMENT)
        ).toEther()

    private fun getPeriod(periodRequest: PeriodServiceRequest): String {
        val fromPeriod = formatToYearMonthDay(periodRequest.from)
        val toPeriod = formatToYearMonthDay(periodRequest.to)
        return "$fromPeriod - $toPeriod"
    }

    private fun formatToYearMonthDay(date: LocalDateTime): String =
        date.format(DateTimeFormatter.ofPattern(DATE_FORMAT).withLocale(locale))

    private fun sumTransactionAmountsByType(type: TransactionType): BigInteger {
        return transactionsByType[type]?.sumOf { it.value } ?: BigInteger.ZERO
    }
}
