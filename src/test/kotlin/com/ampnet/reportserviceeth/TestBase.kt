package com.ampnet.reportserviceeth

import com.ampnet.reportserviceeth.service.ETHER_DECIMALS_PRECISION
import com.ampnet.reportserviceeth.service.MWEI_DECIMALS_PRECISION
import org.springframework.test.context.ActiveProfiles
import java.math.BigInteger

@ActiveProfiles("test")
abstract class TestBase {

    protected fun suppose(@Suppress("UNUSED_PARAMETER") description: String, function: () -> Unit) {
        function.invoke()
    }

    protected fun verify(@Suppress("UNUSED_PARAMETER") description: String, function: () -> Unit) {
        function.invoke()
    }
}

fun String.toWei(): BigInteger = BigInteger(this).times(BigInteger.valueOf(ETHER_DECIMALS_PRECISION))
fun String.toMWei(): BigInteger = BigInteger(this).times(BigInteger.valueOf(MWEI_DECIMALS_PRECISION))
