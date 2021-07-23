package com.ampnet.reportserviceeth.service.data

data class SingleTransactionSummary(
    val transaction: Transaction,
    val userInfo: UserInfo,
    val translations: Translations,
    val logo: String = DEFAULT_LOGO,
    val period: String? = null
)
