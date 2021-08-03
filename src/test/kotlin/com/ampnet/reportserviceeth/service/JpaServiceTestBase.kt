package com.ampnet.reportserviceeth.service

import com.ampnet.identityservice.proto.UserResponse
import com.ampnet.reportserviceeth.TestBase
import com.ampnet.reportserviceeth.blockchain.BlockchainService
import com.ampnet.reportserviceeth.blockchain.TransactionInfo
import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.config.JsonConfig
import com.ampnet.reportserviceeth.grpc.userservice.UserService
import com.ampnet.reportserviceeth.service.impl.TranslationServiceImpl
import com.ampnet.reportserviceeth.toGwei
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime
import java.time.ZonedDateTime

@ExtendWith(SpringExtension::class)
@Import(JsonConfig::class)
abstract class JpaServiceTestBase : TestBase() {

    protected val userAddress = "0x8f52B0cC50967fc59C6289f8FDB3E356EdeEBD23"
    protected val secondUserAddress = "0xd43e088622404A5A21267033EC200383d39C22ca"
    protected val thirdUserAddress = "0x5BF28A1E60Eb56107FAd2dE1F2AA51FC7A60C690"
    protected val userWallet: String = "0x520eC1D2f24740B85c6A30fB9e56298dAd540FDb"
    protected val projectWallet: String = "0xFeC646017105fA2A4FFDc773e9c539Eda5af724a"
    protected val txHash = "0x07b12471d1eac43a429cd38df96671621763f03bdde047697c62c22f5ff9bd37"
    protected val logo = "https://ampnet.io/assets/images/logo-amp.png"
    protected val issuer = "0x5013F6ce0f9Beb07Be528E408352D03f3FCa1857"

    @Mock
    protected lateinit var blockchainService: BlockchainService

    @Mock
    protected lateinit var userService: UserService

    @Autowired
    @Qualifier("camelCaseObjectMapper")
    protected lateinit var camelCaseObjectMapper: ObjectMapper

    protected val translationService: TranslationService by lazy {
        TranslationServiceImpl(camelCaseObjectMapper)
    }

    protected fun createTransaction(
        from: String,
        to: String,
        amount: String,
        type: TransactionType,
        date: LocalDateTime = LocalDateTime.now().minusDays(1),
        txHash: String = "0x07b12471d1eac43a429cd38df96671621763f03bdde047697c62c22f5ff9bd37"
    ): TransactionInfo = TransactionInfo(
        type, from, to, amount.toGwei(), amount.toGwei(),
        date, txHash, "asset"
    )

    protected fun createUserResponse(address: String, language: String = "en"): UserResponse =
        UserResponse.newBuilder()
            .setAddress(address)
            .setFirstName("first name")
            .setLastName("last Name")
            .setCreatedAt(ZonedDateTime.now().minusDays(11).toEpochSecond())
            .setLanguage(language)
            .setDateOfBirth("15.11.1991.")
            .setDocumentNumber("document number")
            .setPersonalNumber("personal number")
            .setDocumentValidFrom("23.07.2021.")
            .setDocumentValidUntil("23.07.2031.")
            .build()
}
