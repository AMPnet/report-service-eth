package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.blockchain.properties.Chain
import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesHandler
import com.ampnet.reportserviceeth.config.ApplicationProperties
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
class BlockchainEventServiceTest {

    @Autowired
    private lateinit var applicationProperties: ApplicationProperties

    @Autowired
    private lateinit var chainPropertiesHandler: ChainPropertiesHandler

    private val service: BlockchainEventService by lazy {
        BlockchainEventServiceImpl(chainPropertiesHandler)
    }

    @Test
    fun mustBeAbleToGetAllEvents() {
        applicationProperties.chainMumbai.maxBlocks = 99_999
        val startBlockNumber = applicationProperties.chainMumbai.startBlockNumber
        val events = service.getAllEvents(
            startBlockNumber, startBlockNumber + applicationProperties.chainMumbai.maxBlocks,
            Chain.MATIC_TESTNET_MUMBAI.id
        )
        assertThat(events).isNotEmpty
    }
}
