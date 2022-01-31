package com.ampnet.reportserviceeth.service

import com.ampnet.identityservice.proto.UserResponse
import com.ampnet.reportserviceeth.TestBase
import com.ampnet.reportserviceeth.blockchain.BlockchainEventService
import com.ampnet.reportserviceeth.blockchain.BlockchainService
import com.ampnet.reportserviceeth.blockchain.TransactionInfo
import com.ampnet.reportserviceeth.blockchain.TransactionType
import com.ampnet.reportserviceeth.blockchain.properties.Chain
import com.ampnet.reportserviceeth.config.ApplicationProperties
import com.ampnet.reportserviceeth.config.JsonConfig
import com.ampnet.reportserviceeth.grpc.userservice.UserService
import com.ampnet.reportserviceeth.persistence.model.Event
import com.ampnet.reportserviceeth.service.impl.TranslationServiceImpl
import com.ampnet.reportserviceeth.util.ChainId
import com.ampnet.reportserviceeth.util.ContractAddress
import com.ampnet.reportserviceeth.util.TransactionHash
import com.ampnet.reportserviceeth.util.WalletAddress
import com.ampnet.reportserviceth.contract.IIssuerCommon
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestTemplate
import org.web3j.utils.Convert
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
@Import(JsonConfig::class, RestTemplate::class, ApplicationProperties::class)
abstract class JpaServiceTestBase : TestBase() {

    protected val userAddress = WalletAddress("0x8f52B0cC50967fc59C6289f8FDB3E356EdeEBD23")
    protected val secondUserAddress = WalletAddress("0xd43e088622404A5A21267033EC200383d39C22ca")
    protected val thirdUserAddress = WalletAddress("0x5BF28A1E60Eb56107FAd2dE1F2AA51FC7A60C690")
    protected val userWallet = WalletAddress("0x520eC1D2f24740B85c6A30fB9e56298dAd540FDb")
    protected val projectWallet = ContractAddress("0xFeC646017105fA2A4FFDc773e9c539Eda5af724a")
    protected val txHash = TransactionHash("0x07b12471d1eac43a429cd38df96671621763f03bdde047697c62c22f5ff9bd37")
    protected val ipfsCid = "QmYuSijGgZAnBadguWUjLTYyfbvpaUBoWRfQMveo6XfzP3"
    protected val issuer = ContractAddress("0x5013F6ce0f9Beb07Be528E408352D03f3FCa1857")
    protected val chainId = Chain.MATIC_TESTNET_MUMBAI.id
    protected val defaultAssetName = "asset_name"
    protected val defaultAssetSymbol = "SMB"
    private val ethUnit = Convert.Unit.ETHER
    private val ethRawDecimals: BigInteger = BigInteger.valueOf(18)
    private val stableCoinUnit = Convert.Unit.MWEI
    protected val stableCoinRawDecimals: BigInteger = BigInteger.valueOf(6)
    protected val stableCoinDecimals: Decimals = stableCoinRawDecimals.toDecimals()

    @Mock
    protected lateinit var blockchainService: BlockchainService

    @Mock
    protected lateinit var blockchainEventService: BlockchainEventService

    @Mock
    protected lateinit var userService: UserService

    @Mock
    protected lateinit var eventService: EventService

    @Mock
    protected lateinit var ipfsService: IpfsService

    @Autowired
    protected lateinit var restTemplate: RestTemplate

    @Autowired
    protected lateinit var applicationProperties: ApplicationProperties

    @Autowired
    @Qualifier("camelCaseObjectMapper")
    protected lateinit var camelCaseObjectMapper: ObjectMapper

    protected val translationService: TranslationService by lazy {
        TranslationServiceImpl(camelCaseObjectMapper)
    }

    protected fun createTransaction(
        from: WalletAddress,
        to: WalletAddress,
        amount: String,
        type: TransactionType,
        date: LocalDateTime = LocalDateTime.now().minusDays(1),
        txHash: TransactionHash = TransactionHash("0x07b12471d1eac43a429cd38df96671621763f03bdde047697c62c22f5ff9bd37")
    ): TransactionInfo = TransactionInfo(
        type, from.value, to.toString(),
        Convert.toWei(amount, stableCoinUnit).toBigInteger(),
        Convert.toWei(amount, ethUnit).toBigInteger(),
        date, txHash.value, "asset", "GMC", ethRawDecimals, stableCoinRawDecimals
    )

    protected fun createUserResponse(address: WalletAddress, language: String = "en"): UserResponse =
        UserResponse.newBuilder()
            .setAddress(address.value)
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

    protected fun createEvent(
        from: WalletAddress = userAddress,
        to: WalletAddress = projectWallet.asWallet(),
        type: TransactionType = TransactionType.COMPLETED_INVESTMENT,
        amount: String = "100.50",
        txHash: TransactionHash = TransactionHash("txHash"),
        chain: ChainId = chainId,
        contractAddress: ContractAddress = projectWallet,
        issuerAddress: ContractAddress = issuer,
        logIndex: Long = 134L,
        blockHash: String = "blockHash",
        localDateTime: LocalDateTime = LocalDateTime.now(),
    ) =
        Event(
            UUID.randomUUID(), chain.value, from.value, to.value,
            contractAddress.value, issuerAddress.value, txHash.value, type,
            logIndex, defaultAssetName, defaultAssetSymbol, 500045L, blockHash,
            localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000,
            Convert.toWei(amount, stableCoinUnit).toBigInteger(),
            ethRawDecimals.toShort(),
            Convert.toWei(amount, ethUnit).toBigInteger(),
            50L, BigInteger("500"), stableCoinRawDecimals.toShort()
        )

    protected fun generateIssuerCommonState() =
        IIssuerCommon.IssuerCommonState(
            "flavor", "version", "0xf9a13b61d15e4eb4046da02d34473f5dc53e5f7c",
            "0x4d2ebc8b12e6f9d5ee6d2412e0651cb0f603c54c", "0x4d2ebc8b12e6f9d5ee6d2412e0651cb0f603c54c",
            "0x4d2ebc8b12e6f9d5ee6d2412e0651cb0f603c54c", ipfsCid
        )
}
