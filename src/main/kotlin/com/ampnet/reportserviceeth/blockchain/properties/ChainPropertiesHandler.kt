package com.ampnet.reportserviceeth.blockchain.properties

import com.ampnet.reportserviceeth.config.ApplicationProperties
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InvalidRequestException
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.ReadonlyTransactionManager

class ChainPropertiesHandler(private val applicationProperties: ApplicationProperties) {

    private val blockchainPropertiesMap = mutableMapOf<Long, ChainPropertiesWithServices>()

    @Throws(InvalidRequestException::class)
    fun getBlockchainProperties(chainId: Long): ChainPropertiesWithServices {
        blockchainPropertiesMap[chainId]?.let { return it }
        val chain = Chain.fromId(chainId)
            ?: throw InvalidRequestException(ErrorCode.BLOCKCHAIN_ID, "Blockchain id: $chainId not supported")
        val properties = generateBlockchainProperties(chain)
        blockchainPropertiesMap[chainId] = properties
        return properties
    }

    private fun generateBlockchainProperties(chain: Chain): ChainPropertiesWithServices {
        val web3j = Web3j.build(HttpService(getChainRpcUrl(chain)))
        return ChainPropertiesWithServices(
            web3j, ReadonlyTransactionManager(web3j, applicationProperties.smartContract.walletAddress)
        )
    }

    internal fun getChainRpcUrl(chain: Chain): String =
        if (chain.infura == null || applicationProperties.infuraId.isBlank()) {
            chain.rpcUrl
        } else {
            "${chain.infura}${applicationProperties.infuraId}"
        }
}
