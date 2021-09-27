package com.ampnet.reportserviceeth.service

import org.junit.jupiter.api.Test
import java.math.BigInteger

class ExtensionsTest {

    @Test
    fun mustFormatCorrectlyMWei() {
        println(BigInteger.valueOf(321543253425423532).toMwei())
        println(BigInteger.valueOf(321543253425423532).toEther())
    }

//    @Test
//    fun mustFormatCorrectlyEther() {
//
//    }
}
