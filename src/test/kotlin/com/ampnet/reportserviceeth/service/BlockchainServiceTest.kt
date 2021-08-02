package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.blockchain.BlockchainService
import com.ampnet.reportserviceeth.blockchain.BlockchainServiceImpl
import com.ampnet.reportserviceeth.config.ApplicationProperties
import com.ampnet.reportserviceeth.exception.InternalException
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

    private val issuerContract = "0xac8211B25CdFE7edF0633a9c3A67d6888C784F1A"
    private val investTxHash = "0x9cfcb44d3516bfc5ecddc03a7c9089815893bd23473cd63833e33b639f7401b1"
    private val cancelInvestmentTxHash = "0xc98df31e24c43c5e5e8be776880c134d990b467ad31dd2e951ffd6f3d599848b"

    @Autowired
    private lateinit var applicationProperties: ApplicationProperties

    private val service: BlockchainService by lazy {
        BlockchainServiceImpl(applicationProperties)
    }

    @Test
    fun mustGetAllWhitelistedAddressesForIssuer() {
        val addresses = service.getWhitelistedAddress(issuerContract)
        assertThat(addresses).isNotEmpty
    }

    @Test
    fun mustGetIssuerOwner() {
        val owner = service.getIssuerOwner(issuerContract)
        assertThat(owner).isNotNull
    }

    @Test
    fun mustBeAbleToGetInvestTransaction() {
        val info = service.getTransactionInfo(investTxHash)
        assertThat(info).isNotNull
    }

    @Test
    fun mustBeAbleToGetCancelInvestmentTransaction() {
        val info = service.getTransactionInfo(cancelInvestmentTxHash)
        assertThat(info).isNotNull
    }

    @Test
    fun mustThrowExceptionForUnknownTxHash() {
        assertThrows<InternalException> {
            service.getTransactionInfo("0xb98df31e24c43c5e5e8be776880c134d990b467ad31dd2e951ffd6f3d599848b")
        }
    }
}
