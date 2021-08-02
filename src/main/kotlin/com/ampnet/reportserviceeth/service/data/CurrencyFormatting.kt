package com.ampnet.reportserviceeth.service.data

import java.math.BigInteger
import java.text.DecimalFormat

const val DECIMALS_PRECISION = 1_000_000_000_000_000_000

fun BigInteger.toEther(): String = DecimalFormat("#,##0.00")
    .format(this / BigInteger(DECIMALS_PRECISION.toString()))
