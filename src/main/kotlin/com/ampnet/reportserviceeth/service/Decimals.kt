package com.ampnet.reportserviceeth.service

import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger

sealed interface Decimals {
    val factor: BigDecimal
}

@JvmInline
private value class RawDecimals(val value: BigInteger) : Decimals {
    override val factor: BigDecimal
        get() {
            println("value: $value")
            return BigDecimal.TEN.pow(value.toInt())
        }
}

@JvmInline
private value class UnitDecimals(val unit: Convert.Unit) : Decimals {
    override val factor: BigDecimal
        get() = unit.weiFactor
}

fun BigInteger.toDecimals(): Decimals = RawDecimals(this)

fun Convert.Unit.toDecimals(): Decimals = UnitDecimals(this)
