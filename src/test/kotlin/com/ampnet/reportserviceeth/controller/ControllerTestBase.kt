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
import com.ampnet.reportserviceeth.util.ChainId
import com.ampnet.reportserviceeth.util.ContractAddress
import com.ampnet.reportserviceeth.util.TransactionHash
import com.ampnet.reportserviceeth.util.WalletAddress
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

    protected final val userAddress = WalletAddress("0x8f52B0cC50967fc59C6289f8FDB3E356EdeEBD23")
    protected final val secondUserAddress = WalletAddress("0xd43e088622404A5A21267033EC200383d39C22ca")
    protected final val thirdUserAddress = WalletAddress("0x5BF28A1E60Eb56107FAd2dE1F2AA51FC7A60C690")
    protected final val projectWallet = ContractAddress("0xFeC646017105fA2A4FFDc773e9c539Eda5af724a")
    protected final val txHash = TransactionHash("0x07b12471d1eac43a429cd38df96671621763f03bdde047697c62c22f5ff9bd37")
    protected final val issuerInfo = "QmQ1wY6jd5uqAcPbdANR6BDqQt8fqEoCc64ypC6dvwnmTb"
    protected final val ipfsHash = "QmYuSijGgZAnBadguWUjLTYyfbvpaUBoWRfQMveo6XfzP3"
    protected final val issuer = ContractAddress("issuer-contract-address")
    protected final val campaign = ContractAddress("campaign-contract-address")
    protected final val defaultChainId = Chain.MATIC_TESTNET_MUMBAI.id
    private final val ethUnit = Convert.Unit.ETHER
    private final val ethDecimals: BigInteger = BigInteger.valueOf(18)
    private final val stableCoinUnit = Convert.Unit.MWEI
    private final val stableCoinDecimals: BigInteger = BigInteger.valueOf(6)

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

    protected fun createUserResponse(address: WalletAddress): UserResponse =
        UserResponse.newBuilder()
            .setAddress(address.value)
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
        Convert.toWei(amount, stableCoinUnit).toBigInteger(),
        Convert.toWei(amount, ethUnit).toBigInteger(),
        LocalDateTime.now(), "0xafd91eb7096efdc4e8ef331a83bc512f279b80730dfbd62824df92e4e504f2f8",
        "Gold mine in Chile", "GMC", ethDecimals, stableCoinDecimals
    )

    protected fun getDownloadDirectory(name: String): String =
        System.getProperty("user.home") + File.separator + "Downloads" + File.separator + name

    protected fun createTransactionsResponse(): List<TransactionInfo> {
        val investment = "30000"
        val invests = MutableList(2) {
            createTransaction(
                TransactionType.RESERVE_INVESTMENT,
                userAddress.value,
                projectWallet.value,
                amount = investment
            )
        }
        val revenueShares =
            MutableList(2) {
                createTransaction(
                    TransactionType.REVENUE_SHARE,
                    projectWallet.value,
                    userAddress.value,
                    amount = "500"
                )
            }
        val cancelInvestments = MutableList(1) {
            createTransaction(
                TransactionType.CANCEL_INVESTMENT,
                projectWallet.value,
                userAddress.value,
                amount = investment
            )
        }
        return invests + revenueShares + cancelInvestments
    }

    protected fun createEvent(
        from: WalletAddress = userAddress,
        to: WalletAddress = projectWallet.asWallet(),
        type: TransactionType = TransactionType.COMPLETED_INVESTMENT,
        amount: String = "70.23",
        value: String = "12.64",
        contractAddress: ContractAddress = projectWallet,
        issuerAddress: ContractAddress = issuer,
        txHash: TransactionHash = TransactionHash(UUID.randomUUID().toString()),
        chain: ChainId = defaultChainId,
        logIndex: Long = 134L,
        blockHash: String = "blockHash",
        localDateTime: LocalDateTime = LocalDateTime.now(),
        saveToDb: Boolean = true
    ): Event {
        val event = Event(
            UUID.randomUUID(), chain.value, from.value, to.value,
            contractAddress.value, issuerAddress.value, txHash.value, type,
            logIndex, "project_name", "symbol", 500045L, blockHash,
            localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000,
            Convert.toWei(amount, stableCoinUnit).toBigInteger(),
            ethDecimals.toShort(),
            Convert.toWei(amount, ethUnit).toBigInteger(),
            50L, BigInteger.valueOf(500), stableCoinDecimals.toShort()
        )
        return if (saveToDb) eventRepository.save(event)
        else event
    }

    protected fun createEventsResponse(): List<Event> {
        val investment = "280.43"
        val amount = "543.10"
        val invests = MutableList(2) {
            createEvent(userAddress, projectWallet.asWallet(), TransactionType.RESERVE_INVESTMENT, investment, amount)
        }
        val cancelInvestments = MutableList(1) {
            createEvent(userAddress, projectWallet.asWallet(), TransactionType.CANCEL_INVESTMENT, investment, amount)
        }
        val revenueShares = MutableList(2) {
            createEvent(projectWallet.asWallet(), userAddress, TransactionType.REVENUE_SHARE, investment, amount)
        }
        return invests + cancelInvestments + revenueShares
    }

    protected fun generateIssuerCommonState() =
        IIssuerCommon.IssuerCommonState(
            "flavor", "version", "0xf9a13b61d15e4eb4046da02d34473f5dc53e5f7c",
            "0x4d2ebc8b12e6f9d5ee6d2412e0651cb0f603c54c", "0x4d2ebc8b12e6f9d5ee6d2412e0651cb0f603c54c",
            "0x4d2ebc8b12e6f9d5ee6d2412e0651cb0f603c54c", "QmaPmqiytVSSHtbmjfWp82VQcE82pq28iHeTL8hmVigscJ"
        )
}
