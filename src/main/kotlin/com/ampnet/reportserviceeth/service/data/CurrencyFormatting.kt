package com.ampnet.reportserviceeth.service.data

import java.math.BigInteger
import java.text.DecimalFormat

const val FROM_CENTS_TO_EUROS = 100.0

fun Long.toEurAmount(): String = DecimalFormat("#,##0.00").format(this / FROM_CENTS_TO_EUROS)

// TODO("this -> Gwei to ether -> format")
fun BigInteger.toEurAmount(): String = DecimalFormat("#,##0.00").format(this.toLong() / FROM_CENTS_TO_EUROS)
