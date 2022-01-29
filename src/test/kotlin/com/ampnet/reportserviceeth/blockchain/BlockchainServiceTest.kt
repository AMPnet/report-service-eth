package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.blockchain.properties.Chain
import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesHandler
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import com.ampnet.reportserviceeth.util.ContractAddress
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

    private val issuerContract = ContractAddress("0xF9a13B61d15E4eB4046DA02d34473F5dc53e5f7c")
    private val chainId = Chain.MATIC_TESTNET_MUMBAI.id

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

    @Test
    fun mustGetIssuerState() {
        val issuerState = service.getIssuerCommonState(Chain.MATIC_TESTNET_MUMBAI.id, issuerContract)
        assertThat(issuerState).isNotNull
    }
}
