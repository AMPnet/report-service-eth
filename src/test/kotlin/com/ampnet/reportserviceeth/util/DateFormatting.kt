package com.ampnet.reportserviceeth.util

import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDateTime.toMiliSeconds(): Long =
    this.toInstant(ZoneOffset.UTC).toEpochMilli()
