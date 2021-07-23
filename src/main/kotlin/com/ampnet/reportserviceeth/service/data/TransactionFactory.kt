package com.ampnet.reportserviceeth.service.data

import com.ampnet.reportserviceeth.blockchain.TransactionInfo
import com.ampnet.reportserviceeth.blockchain.TransactionStatusType
import com.ampnet.reportserviceeth.blockchain.TransactionType
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

const val TO_PERCENTAGE = 100L
const val LENGTH_OF_PERCENTAGE = 8

class TransactionFactory private constructor() {
    companion object {
        fun createTransaction(transaction: TransactionInfo): Transaction? {
            transaction.type.let {
                return when (it) {
                    TransactionType.INVEST -> TransactionInvest(transaction)
                    TransactionType.CLAIM_TOKENS -> TransactionSharePayout(transaction)
                    TransactionType.CANCEL_INVESTMENT -> TransactionCancelInvestment(transaction)
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
    val amount: BigInteger = transaction.amount
    val date: LocalDateTime = transaction.timestamp
    val txDate: String
        get() = date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm").withLocale(locale))
    val amountInEuro: String = amount.toEurAmount()
    abstract val txStatus: TransactionStatusType
    abstract val name: String
    lateinit var translations: Translations

    var description: String? = transaction.asset
    var percentageInProject: String? = null
    var locale: Locale = Locale.ENGLISH

    fun setPercentageInProject(expectedProjectFunding: BigInteger) {
        if (expectedProjectFunding.signum() == 1) {
            percentageInProject = amount
                .multiply(BigInteger.valueOf(TO_PERCENTAGE))
                .divide(expectedProjectFunding)
                .toString().take(LENGTH_OF_PERCENTAGE)
        }
    }

    fun amountToCalculate(): BigInteger {
        return when (txStatus) {
            TransactionStatusType.PAID_IN -> amount
            TransactionStatusType.PAID_OUT -> -amount
            TransactionStatusType.UNDEFINED -> BigInteger.ZERO
        }
    }

    fun setLanguage(language: String) {
        if (language.isNotBlank()) {
            this.locale = Locale.forLanguageTag(language)
        }
    }
}

class TransactionInvest(transaction: TransactionInfo) : Transaction(transaction) {
    override val txStatus = TransactionStatusType.PAID_OUT
    override val name: String
        get() = translations.investment
}

class TransactionCancelInvestment(transaction: TransactionInfo) : Transaction(transaction) {
    override val txStatus = TransactionStatusType.PAID_IN
    override val name: String
        get() = translations.investmentCancel
}

class TransactionSharePayout(transaction: TransactionInfo) : Transaction(transaction) {
    override val txStatus = TransactionStatusType.PAID_IN
    override val name: String
        get() = translations.revenueSharePayout
}
