package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.blockchain.properties.Chain
import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesHandler
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
class BlockchainEventServiceTest {

    private val investTxHash = "0xc63f303d54f23e09d1c191ebd05cecb94c817b5a88582dff14b3817bac43e577"
    private val cancelInvestmentTxHash = "0x179ed82cb4de6ca88711f637576b8cddc7da5fcd75ee368fb61d74880f8a1b56"
    private val chainId = Chain.MATIC_TESTNET_MUMBAI.id

    @Autowired
    private lateinit var applicationProperties: ApplicationProperties

    @Autowired
    private lateinit var chainPropertiesHandler: ChainPropertiesHandler

    private val service: BlockchainEventService by lazy {
        BlockchainEventServiceImpl(chainPropertiesHandler)
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
        applicationProperties.chainMumbai.maxBlocks = 99_999
        val startBlockNumber = 17345957L
        val events = service.getAllEvents(
            startBlockNumber, startBlockNumber + applicationProperties.chainMumbai.maxBlocks,
            Chain.MATIC_TESTNET_MUMBAI.id
        )
        assertThat(events).isNotEmpty
    }
}
