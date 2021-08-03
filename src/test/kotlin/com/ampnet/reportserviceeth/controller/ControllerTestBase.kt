package com.ampnet.reportserviceeth.controller

import com.ampnet.identityservice.proto.UserResponse
import com.ampnet.reportserviceeth.TestBase
import com.ampnet.reportserviceeth.blockchain.BlockchainService
import com.ampnet.reportserviceeth.blockchain.TransactionInfo
import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.ErrorResponse
import com.ampnet.reportserviceeth.grpc.userservice.UserService
import com.ampnet.reportserviceeth.toGwei
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.io.File
import java.time.LocalDateTime
import java.time.ZonedDateTime

@ExtendWith(value = [SpringExtension::class, RestDocumentationExtension::class])
@SpringBootTest
abstract class ControllerTestBase : TestBase() {

    protected val userAddress = "0x8f52B0cC50967fc59C6289f8FDB3E356EdeEBD23"
    protected val secondUserAddress = "0xd43e088622404A5A21267033EC200383d39C22ca"
    protected val thirdUserAddress = "0x5BF28A1E60Eb56107FAd2dE1F2AA51FC7A60C690"
    protected val projectWallet: String = "0xFeC646017105fA2A4FFDc773e9c539Eda5af724a"
    protected val txHash = "0x07b12471d1eac43a429cd38df96671621763f03bdde047697c62c22f5ff9bd37"
    protected val logo = "https://ampnet.io/assets/images/logo-amp.png"
    protected val coop = "ampnet-test"

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @MockBean
    protected lateinit var blockchainService: BlockchainService

    @MockBean
    protected lateinit var userService: UserService

    protected lateinit var mockMvc: MockMvc

    @BeforeEach
    fun init(wac: WebApplicationContext, restDocumentation: RestDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
            .apply<DefaultMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .alwaysDo<DefaultMockMvcBuilder>(
                MockMvcRestDocumentation.document(
                    "{ClassName}/{methodName}",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
                )
            )
            .build()
    }

    protected fun getResponseErrorCode(errorCode: ErrorCode): String {
        return errorCode.categoryCode + errorCode.specificCode
    }

    protected fun verifyResponseErrorCode(result: MvcResult, errorCode: ErrorCode) {
        val response: ErrorResponse = objectMapper.readValue(result.response.contentAsString)
        val expectedErrorCode = getResponseErrorCode(errorCode)
        assert(response.errCode == expectedErrorCode)
    }

    protected fun createUserResponse(address: String): UserResponse =
        UserResponse.newBuilder()
            .setAddress(address)
            .setFirstName("first name")
            .setLastName("last Name")
            .setCreatedAt(ZonedDateTime.now().minusDays(11).toEpochSecond())
            .setLanguage("en")
            .setDateOfBirth("15.11.1991.")
            .setDocumentNumber("document number")
            .setPersonalNumber("personal number")
            .setEmail("email@mail.com")
            .setDocumentValidFrom("23.07.2021.")
            .setDocumentValidUntil("23.07.2031.")
            .build()

    protected fun verifyPdfFormat(data: ByteArray) {
        assertThat(data.isNotEmpty()).isTrue
        assertThat(data.size).isGreaterThan(4)

        // header
        assertThat(data[0]).isEqualTo(0x25) // %
        assertThat(data[1]).isEqualTo(0x50) // P
        assertThat(data[2]).isEqualTo(0x44) // D
        assertThat(data[3]).isEqualTo(0x46) // F
        assertThat(data[4]).isEqualTo(0x2D) // -

        // version is 1.3
        if (data[5].compareTo(0x31) == 0 && data[6].compareTo(0x2E) == 0 && data[7].compareTo(0x33) == 0) {
            // file terminator
            assertThat(data[data.size - 7]).isEqualTo(0x25) // %
            assertThat(data[data.size - 6]).isEqualTo(0x25) // %
            assertThat(data[data.size - 5]).isEqualTo(0x45) // E
            assertThat(data[data.size - 4]).isEqualTo(0x4F) // O
            assertThat(data[data.size - 3]).isEqualTo(0x46) // F
            assertThat(data[data.size - 2]).isEqualTo(0x20) // SPACE
            assertThat(data[data.size - 1]).isEqualTo(0x0A) // EOL
            return
        }

        // version is 1.4
        if (data[5].compareTo(0x31) == 0 && data[6].compareTo(0x2E) == 0 && data[7].compareTo(0x34) == 0) {
            // file terminator
            assertThat(data[data.size - 6]).isEqualTo(0x25) // %
            assertThat(data[data.size - 5]).isEqualTo(0x25) // %
            assertThat(data[data.size - 4]).isEqualTo(0x45) // E
            assertThat(data[data.size - 3]).isEqualTo(0x4F) // O
            assertThat(data[data.size - 2]).isEqualTo(0x46) // F
            assertThat(data[data.size - 1]).isEqualTo(0x0A) // EOL
            return
        }
        fail("Unsupported file format")
    }

    protected fun createTransaction(
        type: TransactionType,
        from: String,
        to: String,
        amount: String = "70"
    ): TransactionInfo = TransactionInfo(
        type, from, to, amount.toGwei(), amount.toGwei(),
        LocalDateTime.now(), "0xafd91eb7096efdc4e8ef331a83bc512f279b80730dfbd62824df92e4e504f2f8",
        "Gold mine in Chile", "APX"
    )

    protected fun getDownloadDirectory(name: String): String =
        System.getProperty("user.home") + File.separator + "Downloads" + File.separator + name

    protected fun createTransactionsResponse(): List<TransactionInfo> {
        val investment = "30000"
        val invests = MutableList(2) {
            createTransaction(
                TransactionType.RESERVE_INVESTMENT,
                userAddress,
                projectWallet,
                amount = investment
            )
        }
        val revenueShares =
            MutableList(2) {
                createTransaction(
                    TransactionType.REVENUE_SHARE,
                    projectWallet,
                    userAddress,
                    amount = "500"
                )
            }
        val cancelInvestments = MutableList(1) {
            createTransaction(
                TransactionType.CANCEL_INVESTMENT,
                projectWallet,
                userAddress,
                amount = investment
            )
        }
        return invests + revenueShares + cancelInvestments
    }
}
