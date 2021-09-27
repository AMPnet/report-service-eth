package com.ampnet.reportserviceeth.controller

import com.ampnet.identityservice.proto.UserResponse
import com.ampnet.reportserviceeth.TestBase
import com.ampnet.reportserviceeth.blockchain.BlockchainService
import com.ampnet.reportserviceeth.blockchain.TransactionInfo
import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.blockchain.properties.Chain
import com.ampnet.reportserviceeth.config.DatabaseCleanerService
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.ErrorResponse
import com.ampnet.reportserviceeth.grpc.userservice.UserService
import com.ampnet.reportserviceeth.persistence.model.Event
import com.ampnet.reportserviceeth.persistence.repository.EventRepository
import com.ampnet.reportserviceeth.service.IpfsService
import com.ampnet.reportserviceth.contract.IIssuerCommon
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
import org.web3j.utils.Convert
import java.io.File
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

@ExtendWith(value = [SpringExtension::class, RestDocumentationExtension::class])
@SpringBootTest
abstract class ControllerTestBase : TestBase() {

    protected val userAddress = "0x8f52B0cC50967fc59C6289f8FDB3E356EdeEBD23"
    protected val secondUserAddress = "0xd43e088622404A5A21267033EC200383d39C22ca"
    protected val thirdUserAddress = "0x5BF28A1E60Eb56107FAd2dE1F2AA51FC7A60C690"
    protected val projectWallet: String = "0xFeC646017105fA2A4FFDc773e9c539Eda5af724a"
    protected val txHash = "0x07b12471d1eac43a429cd38df96671621763f03bdde047697c62c22f5ff9bd37"
    protected val issuerInfo = "QmQ1wY6jd5uqAcPbdANR6BDqQt8fqEoCc64ypC6dvwnmTb"
    protected val ipfsHash = "QmYuSijGgZAnBadguWUjLTYyfbvpaUBoWRfQMveo6XfzP3"
    protected val issuer = "issuer-contract-address"
    protected val defaultChainId = Chain.MATIC_TESTNET_MUMBAI.id

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var databaseCleanerService: DatabaseCleanerService

    @Autowired
    protected lateinit var eventRepository: EventRepository

    @MockBean
    protected lateinit var blockchainService: BlockchainService

    @MockBean
    protected lateinit var userService: UserService

    @MockBean
    protected lateinit var ipfsService: IpfsService

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
        type, from, to,
        Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger(),
        Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger(),
        LocalDateTime.now(), "0xafd91eb7096efdc4e8ef331a83bc512f279b80730dfbd62824df92e4e504f2f8",
        "Gold mine in Chile", "GMC"
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

    protected fun createEvent(
        from: String = userAddress,
        to: String = projectWallet,
        type: TransactionType = TransactionType.COMPLETED_INVESTMENT,
        amount: BigInteger = BigInteger.valueOf(700000),
        value: BigInteger = BigInteger.valueOf(1431433242432),
        contractAddress: String = projectWallet,
        issuerAddress: String = issuer,
        txHash: String = UUID.randomUUID().toString(),
        chain: Long = defaultChainId,
        logIndex: Long = 134L,
        blockHash: String = "blockHash",
        localDateTime: LocalDateTime = LocalDateTime.now(),
        saveToDb: Boolean = true
    ): Event {
        val event = Event(
            UUID.randomUUID(), chain, from.lowercase(), to.lowercase(),
            contractAddress, issuerAddress, txHash, type,
            logIndex, "project_name", "symbol", 500045L, blockHash,
            localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000,
            value, amount, 50L, BigInteger.valueOf(500)
        )
        return if (saveToDb) eventRepository.save(event)
        else event
    }

    protected fun createEventsResponse(): List<Event> {
        val investment = BigInteger.valueOf(24234242423)
        val amount = BigInteger.valueOf(52542548456)
        val invests = MutableList(2) {
            createEvent(userAddress, projectWallet, TransactionType.RESERVE_INVESTMENT, investment, amount)
        }
        val cancelInvestments = MutableList(1) {
            createEvent(userAddress, projectWallet, TransactionType.CANCEL_INVESTMENT, investment, amount)
        }
        val revenueShares = MutableList(2) {
            createEvent(projectWallet, userAddress, TransactionType.REVENUE_SHARE, investment, amount)
        }
        return invests + cancelInvestments + revenueShares
    }

    protected fun generateIssuerCommonState() =
        IIssuerCommon.IssuerCommonState(
            "flavor", "version", "0xf9a13b61d15e4eb4046da02d34473f5dc53e5f7c",
            "0x4d2ebc8b12e6f9d5ee6d2412e0651cb0f603c54c", "QmaPmqiytVSSHtbmjfWp82VQcE82pq28iHeTL8hmVigscJ"
        )
}
