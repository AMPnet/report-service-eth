package com.ampnet.reportserviceeth.service

import com.ampnet.identityservice.proto.UserResponse
import com.ampnet.reportserviceeth.TestBase
import com.ampnet.reportserviceeth.blockchain.TransactionInfo
import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.service.data.DATE_FORMAT
import com.ampnet.reportserviceeth.service.data.Transaction
import com.ampnet.reportserviceeth.service.data.TransactionFactory
import com.ampnet.reportserviceeth.service.data.TransactionsSummary
import com.ampnet.reportserviceeth.service.data.Translations
import com.ampnet.reportserviceeth.service.data.UserInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TransactionsSummaryTest : TestBase() {

    private val userAddress = "0x8f52B0cC50967fc59C6289f8FDB3E356EdeEBD23"

    @Test
    fun mustSetCorrectPeriodAndDateOfFinish() {
        val periodRequest = PeriodServiceRequest(
            LocalDate.of(2020, 7, 1),
            LocalDate.of(2020, 9, 1)
        )
        val txSummary = TransactionsSummary(
            createTransactions().mapNotNull { it },
            UserInfo(createUserResponse()),
            periodRequest,
            Translations(mapOf())
        )
        assertThat(txSummary.period).isEqualTo(getPeriod(periodRequest))
        assertThat(txSummary.dateOfFinish).isEqualTo(getDateOfFinish(periodRequest))
    }

    @Test
    fun mustSetCorrectPeriodAndDateOfFinishForZeroTransactionsAndNullPeriodRequest() {
        val periodRequest = PeriodServiceRequest(null, null)
        val userInfo = UserInfo(createUserResponse())
        val txSummary = TransactionsSummary(listOf(), userInfo, periodRequest, Translations(mapOf()))
        assertThat(txSummary.period).isEqualTo(getPeriodZeroTx(userInfo.createdAt))
        assertThat(txSummary.dateOfFinish).isEqualTo(getDateOfFinish(periodRequest))
    }

    private fun getPeriod(period: PeriodServiceRequest): String {
        return formatToYearMonthDay(period.from) + " - " + formatToYearMonthDay(period.to)
    }

    private fun getPeriodZeroTx(createdAt: LocalDateTime): String {
        return formatToYearMonthDay(createdAt) + " - " + formatToYearMonthDay(LocalDateTime.now())
    }

    private fun getDateOfFinish(period: PeriodServiceRequest): String {
        return if (period.to == null) formatToYearMonthDay(LocalDateTime.now())
        else formatToYearMonthDay(period.to)
    }

    private fun formatToYearMonthDay(date: LocalDateTime?): String {
        return date!!.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
    }

    private fun createUserResponse(address: String = userAddress): UserResponse {
        return UserResponse.newBuilder()
            .setAddress(address)
            .setFirstName("First")
            .setLastName("Last")
            .build()
    }

    private fun createTransaction(date: LocalDateTime): TransactionInfo = TransactionInfo(
        TransactionType.RESERVE_INVESTMENT,
        "0x8f52B0cC50967fc59C6289f8FDB3E356EdeEBD23",
        "0xd43e088622404A5A21267033EC200383d39C22ca",
        BigInteger.TEN,
        BigInteger.TEN,
        date,
        "0x07b12471d1eac43a429cd38df96671621763f03bdde047697c62c22f5ff9bd37",
        "asset"
    )

    private fun createTransactions(): List<Transaction?> {
        return listOf(
            TransactionFactory.createTransaction(
                createTransaction(LocalDateTime.of(2020, 10, 1, 0, 0, 0, 0))
            ),
            TransactionFactory.createTransaction(
                createTransaction(LocalDateTime.of(2020, 9, 1, 0, 0, 0, 0))
            ),
            TransactionFactory.createTransaction(
                createTransaction(LocalDateTime.of(2020, 8, 1, 0, 0, 0, 0))
            ),
            TransactionFactory.createTransaction(
                createTransaction(LocalDateTime.of(2020, 7, 1, 0, 0, 0, 0))
            )
        )
    }
}
