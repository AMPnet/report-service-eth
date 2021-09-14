package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.blockchain.properties.Chain
import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesHandler
import com.ampnet.reportserviceeth.config.ApplicationProperties
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InternalException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ChainPropertiesHandlerTest {

    @Test
    fun mustThrowExceptionForInvalidChainId() {
        val chainPropertiesHandler = ChainPropertiesHandler(ApplicationProperties())
        val exception = assertThrows<InternalException> {
            chainPropertiesHandler.getBlockchainProperties(-1)
        }
        assertThat(exception.errorCode).isEqualTo(ErrorCode.BLOCKCHAIN_ID)
    }

    @Test
    fun mustReturnDefaultRpcIfInfuraIdIsMissing() {
        val applicationProperties = ApplicationProperties().apply { infuraId = "" }
        val chainPropertiesHandler = ChainPropertiesHandler(applicationProperties)
        val chain = Chain.MATIC_TESTNET_MUMBAI
        val rpc = chainPropertiesHandler.getChainRpcUrl(chain)
        assertThat(rpc).isEqualTo(chain.rpcUrl)
    }

    @Test
    fun mustReturnInfuraRpc() {
        val infuraId = "some-id"
        val applicationProperties = ApplicationProperties().apply { this.infuraId = infuraId }
        val chainPropertiesHandler = ChainPropertiesHandler(applicationProperties)
        val chain = Chain.ETHEREUM_MAIN
        val rpc = chainPropertiesHandler.getChainRpcUrl(chain)
        assertThat(rpc).isEqualTo(chain.infura + infuraId)
    }
}
