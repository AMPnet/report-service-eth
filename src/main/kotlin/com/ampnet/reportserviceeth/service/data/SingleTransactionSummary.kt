package com.ampnet.reportserviceeth.service.data

import com.ampnet.reportserviceeth.service.formatToYearMonthDay
import java.util.Locale

class SingleTransactionSummary(
    val transaction: Transaction,
    val userInfo: UserInfo,
    val translations: Translations,
    val logo: String = DEFAULT_LOGO
) {
    private val locale: Locale = if (userInfo.language.isBlank()) {
        Locale.ENGLISH
    } else {
        Locale.forLanguageTag(userInfo.language)
    }
    val period: String = transaction.date.formatToYearMonthDay(locale)
}
