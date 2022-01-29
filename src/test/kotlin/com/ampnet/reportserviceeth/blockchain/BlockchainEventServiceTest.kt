package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.blockchain.properties.Chain
import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesHandler
import com.ampnet.reportserviceeth.config.ApplicationProperties
import com.ampnet.reportserviceeth.util.BlockNumber
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigInteger

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
        applicationProperties.chainMumbai.maxBlocks = BigInteger.valueOf(99_999L)
        val startBlockNumber = applicationProperties.chainMumbai.startBlockNumber
        val events = service.getAllEvents(
            BlockNumber(startBlockNumber), BlockNumber(startBlockNumber + applicationProperties.chainMumbai.maxBlocks),
            Chain.MATIC_TESTNET_MUMBAI.id
        )
        assertThat(events).isNotEmpty
    }
}
