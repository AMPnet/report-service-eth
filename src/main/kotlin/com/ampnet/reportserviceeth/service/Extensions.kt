package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.service.data.DATE_FORMAT
import mu.KotlinLogging
import org.web3j.protocol.core.RemoteFunctionCall
import org.web3j.protocol.core.Request
import org.web3j.protocol.core.Response
import org.web3j.utils.Convert
import java.io.IOException
import java.math.BigInteger
import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.Optional

const val NUMBER_FORMAT = "#,##0.00"

private val logger = KotlinLogging.logger {}

fun <T> Optional<T>.unwrap(): T? = if (this.isPresent) this.get() else null

@Suppress("ReturnCount")
fun <S, T : Response<*>?> Request<S, T>.sendSafely(): T? {
    try {
        val value = this.send()
        if (value?.hasError() == true) {
            logger.warn { "Errors: ${value.error.message}" }
            return null
        }
        return value
    } catch (ex: IOException) {
        logger.warn("Failed blockchain call", ex)
        return null
    }
}

@Suppress("TooGenericExceptionCaught")
fun <T> RemoteFunctionCall<T>.sendSafely(): T? {
    return try {
        this.send()
    } catch (ex: Exception) {
        logger.warn("Failed smart contract call", ex)
        null
    }
}

fun BigInteger.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochSecond(this.toLong()), ZoneId.systemDefault())

fun Long.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneId.systemDefault())

fun BigInteger.toEther(): String = DecimalFormat(NUMBER_FORMAT)
    .format(Convert.fromWei(this.toBigDecimal(), Convert.Unit.ETHER))

fun BigInteger.toMwei(): String = DecimalFormat(NUMBER_FORMAT)
    .format(Convert.fromWei(this.toBigDecimal(), Convert.Unit.MWEI))

@Suppress("MagicNumber")
fun Long.toTimestamp(): Long = this * 1000

fun LocalDateTime.formatToYearMonthDay(locale: Locale): String =
    this.format(DateTimeFormatter.ofPattern(DATE_FORMAT).withLocale(locale))
