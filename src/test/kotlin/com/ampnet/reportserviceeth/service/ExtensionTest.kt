package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.TestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.web3j.utils.Convert
import java.math.BigInteger
import java.text.DecimalFormat

class ExtensionTest : TestBase() {

    companion object {
        const val NUMBER_FORMAT = "#,##0.00"
    }

    @Test
    fun mustCorrectlyFormatBigIntegerFromWei() {
        val value = BigInteger("12345678901234567890")

        verify("BigInteger values are correctly formatted from wei") {
            assertCorrectlyFormatted(value, 0, Convert.Unit.WEI)
            assertCorrectlyFormatted(value, 3, Convert.Unit.KWEI)
            assertCorrectlyFormatted(value, 6, Convert.Unit.MWEI)
            assertCorrectlyFormatted(value, 9, Convert.Unit.GWEI)
            assertCorrectlyFormatted(value, 12, Convert.Unit.SZABO)
            assertCorrectlyFormatted(value, 15, Convert.Unit.FINNEY)
            assertCorrectlyFormatted(value, 18, Convert.Unit.ETHER)
            assertCorrectlyFormatted(value, 21, Convert.Unit.KETHER)
            assertCorrectlyFormatted(value, 24, Convert.Unit.METHER)
            assertCorrectlyFormatted(value, 27, Convert.Unit.GETHER)
        }
    }

    private fun assertCorrectlyFormatted(value: BigInteger, decimals: Long, unit: Convert.Unit) {
        assertThat(value.formatWei(BigInteger.valueOf(decimals).toDecimals())).isEqualTo(value.formatFromUnit(unit))
    }

    private fun BigInteger.formatFromUnit(unit: Convert.Unit) =
        DecimalFormat(NUMBER_FORMAT).format(Convert.fromWei(this.toBigDecimal(), unit))
}
