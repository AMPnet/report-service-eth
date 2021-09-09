package com.ampnet.reportserviceeth.service.data

data class Translations(val translations: Map<String, String>) {
    val transactions: String by translations
    val walletAddress: String by translations
    val deposit: String by translations
    val amount: String by translations
    val date: String by translations
    val type: String by translations
    val paidIn: String by translations
    val paidOut: String by translations
    val pending: String by translations
    val period: String by translations
    val transactionsStatement: String by translations
    val accountSummary: String by translations
    val deposits: String by translations
    val withdrawals: String by translations
    val totalRevenue: String by translations
    val totalInvestments: String by translations
    val marketplaceBought: String by translations
    val marketplaceSold: String by translations
    val totalBalanceAsOf: String by translations
    val reserveInvestment: String by translations
    val cancelInvestment: String by translations
    val revenueSharePayout: String by translations
    val investmentCompleted: String by translations
    val withdraw: String by translations
    val dateOfBirth: String by translations
    val documentNumber: String by translations
    val dateOfIssue: String by translations
    val dateOfExpiry: String by translations
    val personalNumber: String by translations
    val project: String by translations
    val tokenQuantity: String by translations
}
