package com.ampnet.reportserviceeth.blockchain.properties

import com.ampnet.reportserviceeth.config.ApplicationProperties
import com.ampnet.reportserviceeth.config.ChainProperties
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.exception.InvalidRequestException
import org.springframework.stereotype.Service
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.ReadonlyTransactionManager

@Service
class ChainPropertiesHandler(private val applicationProperties: ApplicationProperties) {

    private val blockchainPropertiesMap = mutableMapOf<Long, ChainPropertiesWithServices>()

    @Throws(InvalidRequestException::class)
    fun getBlockchainProperties(chainId: Long): ChainPropertiesWithServices {
        blockchainPropertiesMap[chainId]?.let { return it }
        val chain = getChain(chainId)
        val properties = generateBlockchainProperties(chain)
        blockchainPropertiesMap[chainId] = properties
        return properties
    }

    fun getChainProperties(chain: Chain): ChainProperties? {
        val chainProperties = when (chain) {
            Chain.MATIC_MAIN -> applicationProperties.chainMatic
            Chain.MATIC_TESTNET_MUMBAI -> applicationProperties.chainMumbai
            Chain.ETHEREUM_MAIN -> applicationProperties.chainEthereum
            Chain.HARDHAT_TESTNET -> applicationProperties.chainHardhatTestnet
        }
        return if (chainProperties.callerAddress.isBlank() ||
            chainProperties.cfManagerFactoryAddress.isEmpty()
        ) {
            null
        } else {
            chainProperties
        }
    }

    private fun generateBlockchainProperties(chain: Chain): ChainPropertiesWithServices {
        val chainProperties = getChainProperties(chain) ?: throw InternalException(
            ErrorCode.BLOCKCHAIN_CONFIG_MISSING,
            "Config for chain: ${chain.name} not defined in the application properties"
        )
        val web3j = Web3j.build(HttpService(getChainRpcUrl(chain)))
        return ChainPropertiesWithServices(
            web3j, ReadonlyTransactionManager(web3j, chainProperties.callerAddress), chainProperties
        )
    }

    private fun getChain(chainId: Long) = Chain.fromId(chainId)
        ?: throw InternalException(ErrorCode.BLOCKCHAIN_ID, "Blockchain id: $chainId not supported")

    internal fun getChainRpcUrl(chain: Chain): String =
        if (chain.infura == null || applicationProperties.infuraId.isBlank()) {
            chain.rpcUrl
        } else {
            "${chain.infura}${applicationProperties.infuraId}"
        }
}
