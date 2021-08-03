package com.ampnet.reportserviceeth

import com.ampnet.reportserviceeth.service.DECIMALS_PRECISION
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

fun String.toGwei(): BigInteger = BigInteger(this).times(BigInteger.valueOf(DECIMALS_PRECISION))
