package com.ampnet.reportserviceeth.service.data

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class SingleTransactionSummary(
    val transaction: Transaction,
    val userInfo: UserInfo,
    val translations: Translations,
    val txCreatedAt: LocalDateTime,
    val logo: String = DEFAULT_LOGO
) {
    private val locale: Locale = if (userInfo.language.isBlank()) {
        Locale.ENGLISH
    } else {
        Locale.forLanguageTag(userInfo.language)
    }
    val period: String = formatToYearMonthDay(txCreatedAt)

    private fun formatToYearMonthDay(date: LocalDateTime): String =
        date.format(DateTimeFormatter.ofPattern(DATE_FORMAT).withLocale(locale))
}
