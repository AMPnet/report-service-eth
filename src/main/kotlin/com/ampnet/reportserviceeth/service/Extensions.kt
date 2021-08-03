package com.ampnet.reportserviceeth.service

import mu.KotlinLogging
import org.web3j.protocol.core.RemoteFunctionCall
import org.web3j.protocol.core.Request
import org.web3j.protocol.core.Response
import java.io.IOException
import java.math.BigInteger
import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDateTime
import java.util.Optional
import java.util.TimeZone

const val DECIMALS_PRECISION = 1_000_000_000_000_000_000

private val logger = KotlinLogging.logger {}

fun <T> Optional<T>.unwrap(): T? = if (this.isPresent) this.get() else null

fun <S, T : Response<*>?> Request<S, T>.sendSafely(): T? {
    return try {
        val value = this.send()
        if (value?.hasError() == true) {
            logger.warn { "Errors: ${value.error.message}" }
            return null
        }
        value
    } catch (ex: IOException) {
        logger.warn("Failed blockchain call", ex)
        null
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
    LocalDateTime.ofInstant(Instant.ofEpochSecond(this.toLong()), TimeZone.getDefault().toZoneId())

fun BigInteger.toEther(): String = DecimalFormat("#,##0.00")
    .format(this / BigInteger.valueOf(DECIMALS_PRECISION))
