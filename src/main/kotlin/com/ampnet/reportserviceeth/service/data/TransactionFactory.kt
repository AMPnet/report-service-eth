package com.ampnet.reportserviceeth.service.data

import com.ampnet.reportserviceeth.blockchain.TransactionInfo
import com.ampnet.reportserviceeth.blockchain.TransactionStatusType
import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.service.formatWei
import com.ampnet.reportserviceeth.service.toEther
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class TransactionFactory private constructor() {
    companion object {
        fun createTransaction(transaction: TransactionInfo): Transaction? {
            transaction.type.let {
                return when (it) {
                    TransactionType.RESERVE_INVESTMENT -> TransactionReserveInvestment(transaction)
                    TransactionType.COMPLETED_INVESTMENT -> TransactionInvestmentCompleted(transaction)
                    TransactionType.CANCEL_INVESTMENT -> TransactionCancelInvestment(transaction)
                    TransactionType.REVENUE_SHARE -> TransactionSharePayout(transaction)
                    else -> null
                }
            }
        }
    }
}

abstract class Transaction(transaction: TransactionInfo) {

    val type: TransactionType = transaction.type
    val from: String = transaction.from
    val to: String = transaction.to
    val amount: BigInteger? = transaction.tokenAmount
    val value: BigInteger = transaction.tokenValue
    val date: LocalDateTime = transaction.timestamp
    val txDate: String
        get() = date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm").withLocale(locale))
    val decimals: BigInteger = transaction.decimals
    val valueInDollar: String = value.formatWei(decimals)
    val amountInEther: String? = amount?.toEther()
    abstract val txStatus: TransactionStatusType
    abstract val name: String
    lateinit var translations: Translations

    var description: String? = transaction.asset
    var assetTokenSymbol: String? = transaction.assetTokenSymbol
    var percentageInProject: String? = null
    var locale: Locale = Locale.ENGLISH

    fun setLanguage(language: String) {
        if (language.isNotBlank()) {
            this.locale = Locale.forLanguageTag(language)
        }
    }
}

class TransactionReserveInvestment(transaction: TransactionInfo) : Transaction(transaction) {
    override val txStatus = TransactionStatusType.PAID_OUT
    override val name: String
        get() = translations.reserveInvestment
}

class TransactionInvestmentCompleted(transaction: TransactionInfo) : Transaction(transaction) {
    override val txStatus = TransactionStatusType.PAID_OUT
    override val name: String
        get() = translations.investmentCompleted
}

class TransactionCancelInvestment(transaction: TransactionInfo) : Transaction(transaction) {
    override val txStatus = TransactionStatusType.PAID_IN
    override val name: String
        get() = translations.cancelInvestment
}

class TransactionSharePayout(transaction: TransactionInfo) : Transaction(transaction) {
    override val txStatus = TransactionStatusType.PAID_IN
    override val name: String
        get() = translations.revenueSharePayout
}
