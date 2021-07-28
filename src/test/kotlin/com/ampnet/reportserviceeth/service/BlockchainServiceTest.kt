package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.blockchain.BlockchainService
import com.ampnet.reportserviceeth.blockchain.BlockchainServiceImpl
import com.ampnet.reportserviceeth.config.ApplicationProperties
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ActiveProfiles("secret")
@ExtendWith(SpringExtension::class)
@Disabled("Not for automated testing")
class BlockchainServiceTest {

    private val issuerContract = "0xac8211B25CdFE7edF0633a9c3A67d6888C784F1A"

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
}
