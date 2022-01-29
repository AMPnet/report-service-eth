package com.ampnet.reportserviceeth

import com.ampnet.reportserviceeth.service.NUMBER_FORMAT
import com.ampnet.reportserviceeth.util.BlockNumber
import com.ampnet.reportserviceeth.util.ChainId
import com.ampnet.reportserviceeth.util.ContractAddress
import com.ampnet.reportserviceeth.util.TransactionHash
import com.ampnet.reportserviceeth.util.WalletAddress
import org.mockito.kotlin.argThat
import org.mockito.kotlin.eq
import org.springframework.test.context.ActiveProfiles
import org.web3j.utils.Convert
import java.math.BigInteger
import java.text.DecimalFormat

@ActiveProfiles("test")
abstract class TestBase {

    protected fun suppose(@Suppress("UNUSED_PARAMETER") description: String, function: () -> Unit) {
        function.invoke()
    }

    protected fun verify(@Suppress("UNUSED_PARAMETER") description: String, function: () -> Unit) {
        function.invoke()
    }

    // Fixes for Mockito (Mockito-Kotlin), because they STILL don't support value classes... in 2022... why even bother
    // maintaining a mocking library if you don't intent to support new language features?
    // https://github.com/mockito/mockito-kotlin/issues/309
    protected inline fun <reified T : Any> anyValueClass(unitValue: T): T {
        argThat<T> { true }
        return unitValue
    }

    protected fun TransactionHash.mockito(): TransactionHash {
        eq(value)
        return this
    }

    protected fun WalletAddress.mockito(): WalletAddress {
        eq(value)
        return this
    }

    protected fun ContractAddress.mockito(): ContractAddress {
        eq(value)
        return this
    }

    protected fun BlockNumber.mockito(): BlockNumber {
        eq(value)
        return this
    }

    protected fun ChainId.mockito(): ChainId {
        eq(value)
        return this
    }
}

fun BigInteger.toMwei(): String = DecimalFormat(NUMBER_FORMAT)
    .format(Convert.fromWei(this.toBigDecimal(), Convert.Unit.MWEI))

fun BigInteger.toWei(unit: Convert.Unit): BigInteger = Convert.toWei(this.toBigDecimal(), unit).toBigInteger()
