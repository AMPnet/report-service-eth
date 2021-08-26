package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.blockchain.BlockchainService
import com.ampnet.reportserviceeth.blockchain.BlockchainServiceImpl
import com.ampnet.reportserviceeth.blockchain.properties.Chain
import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesHandler
import com.ampnet.reportserviceeth.config.ApplicationProperties
import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
@Disabled("Not for automated testing")
class BlockchainServiceTest {

    private val issuerContract = "0x521b0200138cef507769f6d8e8d4999f60b6b319"
    private val investTxHash = "0xc63f303d54f23e09d1c191ebd05cecb94c817b5a88582dff14b3817bac43e577"
    private val cancelInvestmentTxHash = "0x179ed82cb4de6ca88711f637576b8cddc7da5fcd75ee368fb61d74880f8a1b56"
    private val chainId = Chain.MATIC_TESTNET_MUMBAI.id

    @Autowired
    private lateinit var applicationProperties: ApplicationProperties

    @Autowired
    private lateinit var chainPropertiesHandler: ChainPropertiesHandler

    private val service: BlockchainService by lazy {
        BlockchainServiceImpl(applicationProperties, chainPropertiesHandler)
    }

    @Test
    fun mustGetAllWhitelistedAddressesForIssuer() {
        val addresses = service.getWhitelistedAddress(IssuerRequest(issuerContract, chainId))
        assertThat(addresses).isNotEmpty
    }

    @Test
    fun mustGetIssuerOwner() {
        val owner = service.getIssuerOwner(IssuerRequest(issuerContract, chainId))
        assertThat(owner).isNotNull
    }

    @Test
    fun mustBeAbleToGetInvestTransaction() {
        val info = service.getTransactionInfo(investTxHash, chainId)
        assertThat(info).isNotNull
    }

    @Test
    fun mustBeAbleToGetCancelInvestmentTransaction() {
        val info = service.getTransactionInfo(cancelInvestmentTxHash, chainId)
        assertThat(info).isNotNull
    }

    @Test
    fun mustThrowExceptionForUnknownTxHash() {
        assertThrows<InternalException> {
            service.getTransactionInfo("0xb98df31e24c43c5e5e8be776880c134d990b467ad31dd2e951ffd6f3d599848b", chainId)
        }
    }

    @Test
    fun mustBeAbleToGetAllEvents() {
        applicationProperties.queue.maxBlocks = 99_999
        val startBlockNumber = 17345957L
        val events = service.getAllEvents(
            startBlockNumber, startBlockNumber + applicationProperties.queue.maxBlocks,
            Chain.MATIC_TESTNET_MUMBAI.id
        )
        assertThat(events).isNotEmpty
    }
}
