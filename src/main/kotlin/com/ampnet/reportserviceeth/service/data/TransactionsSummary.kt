package com.ampnet.reportserviceeth.service.data

import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.service.Decimals
import com.ampnet.reportserviceeth.service.formatToYearMonthDay
import com.ampnet.reportserviceeth.service.formatWei
import com.ampnet.reportserviceeth.service.toDecimals
import mu.KLogging
import org.web3j.utils.Convert
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.Locale

const val DATE_FORMAT = "MMM dd, yyyy"

class TransactionsSummary(
    val transactions: List<Transaction>,
    val userInfo: UserInfo,
    val periodRequest: PeriodServiceRequest,
    val translations: Translations,
    val logo: String? = null
) {
    companion object : KLogging()

    private val transactionsByType = transactions.groupBy { it.type }
    private val locale: Locale = if (userInfo.language.isBlank()) {
        Locale.ENGLISH
    } else {
        Locale.forLanguageTag(userInfo.language)
    }
    val period: String = getPeriod(periodRequest)
    val dateOfFinish: String = getDateOfFinish(transactions, periodRequest)
    val revenueShare = sumTransactionAmountsByType(TransactionType.REVENUE_SHARE)
        .formatWei(getDecimals(TransactionType.REVENUE_SHARE))
    val investments = (
        sumTransactionAmountsByType(TransactionType.RESERVE_INVESTMENT) -
            sumTransactionAmountsByType(TransactionType.CANCEL_INVESTMENT)
        ).formatWei(getDecimals(TransactionType.RESERVE_INVESTMENT))

    private fun getPeriod(periodRequest: PeriodServiceRequest): String {
        val fromPeriod = (periodRequest.from ?: userInfo.createdAt).formatToYearMonthDay(locale)
        val toPeriod = (periodRequest.to ?: LocalDateTime.now()).formatToYearMonthDay(locale)
        return "$fromPeriod - $toPeriod"
    }

    private fun getDateOfFinish(transactions: List<Transaction>, periodRequest: PeriodServiceRequest): String =
        periodRequest.to?.formatToYearMonthDay(locale)
            ?: if (transactions.isEmpty()) LocalDateTime.now().formatToYearMonthDay(locale)
            else transactions.last().date.formatToYearMonthDay(locale)

    private fun sumTransactionAmountsByType(type: TransactionType): BigInteger =
        transactionsByType[type]?.sumOf { it.value } ?: BigInteger.ZERO

    private fun getDecimals(type: TransactionType): Decimals =
        transactionsByType[type]?.firstOrNull()?.stableCoinDecimals?.toDecimals() ?: Convert.Unit.ETHER.toDecimals()
}
