package com.ampnet.reportserviceeth.service.data

data class UsersAccountsSummary(
    val summaries: List<TransactionsSummary>,
    val logo: String = DEFAULT_LOGO
)
