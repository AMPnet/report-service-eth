package com.ampnet.reportserviceeth.controller.pojo

import java.time.LocalDateTime

data class PeriodServiceRequest(
    val from: LocalDateTime,
    val to: LocalDateTime
)
