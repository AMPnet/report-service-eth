package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.blockchain.properties.Chain
import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesHandler
import com.ampnet.reportserviceeth.config.ApplicationProperties
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
@Disabled("Not for automated testing")
class BlockchainServiceTest {

    private val issuerContract = "0x521b0200138cef507769f6d8e8d4999f60b6b319"
    private val chainId = Chain.MATIC_TESTNET_MUMBAI.id

    @Autowired
    private lateinit var applicationProperties: ApplicationProperties

    @Autowired
    private lateinit var chainPropertiesHandler: ChainPropertiesHandler

    private val service: BlockchainService by lazy {
        BlockchainServiceImpl(chainPropertiesHandler)
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
    fun mustGetBlockNumber() {
        val blockNumber = service.getBlockNumber(Chain.MATIC_TESTNET_MUMBAI.id)
        assertThat(blockNumber).isNotNull
    }
}
