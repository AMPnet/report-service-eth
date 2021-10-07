package com.ampnet.reportserviceeth

import com.ampnet.reportserviceeth.service.NUMBER_FORMAT
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
}

fun BigInteger.toMwei(): String = DecimalFormat(NUMBER_FORMAT)
    .format(Convert.fromWei(this.toBigDecimal(), Convert.Unit.MWEI))

fun BigInteger.toWei(unit: Convert.Unit): BigInteger = Convert.toWei(this.toBigDecimal(), unit).toBigInteger()
