package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.TestBase
import com.ampnet.reportserviceeth.blockchain.TransactionInfo
import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.service.data.TransactionFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.time.LocalDateTime

class TransactionFactoryTest : TestBase() {

    @Test
    fun mustNotCreateApprovedInvestmentTransaction() {
        val tx = TransactionFactory.createTransaction(
            createTransaction(
                type = TransactionType.TOKEN_TRANSFER
            )
        )
        assertThat(tx).isNull()
    }

    @Test
    fun mustNotCreateUnrecognizedTransaction() {
        val tx = TransactionFactory.createTransaction(
            createTransaction(
                type = TransactionType.REVENUE_SHARE
            )
        )
        assertThat(tx).isNull()
    }

    private fun createTransaction(type: TransactionType): TransactionInfo = TransactionInfo(
        type,
        "0x8f52B0cC50967fc59C6289f8FDB3E356EdeEBD23",
        "0xd43e088622404A5A21267033EC200383d39C22ca",
        BigInteger.TEN,
        BigInteger.TEN,
        LocalDateTime.now(),
        "0x07b12471d1eac43a429cd38df96671621763f03bdde047697c62c22f5ff9bd37",
        "asset"
    )
}
