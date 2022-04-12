package com.ampnet.reportserviceeth.blockchain.properties

import com.ampnet.reportserviceeth.util.ChainId

@Suppress("MagicNumber")
enum class Chain(val id: ChainId, val rpcUrl: String, val infura: String?) {
    MATIC_MAIN(ChainId(137L), "https://polygon-rpc.com/", null),
    MATIC_TESTNET_MUMBAI(ChainId(80001L), "https://matic-mumbai.chainstacklabs.com/", null),
    ETHEREUM_MAIN(ChainId(1L), "https://cloudflare-eth.com/", "https://mainnet.infura.io/v3/"),
    GOERLI_TESTNET(ChainId(5L), "https://goerli.prylabs.net/", "https://goerli.infura.io/v3/"),
    HARDHAT_TESTNET(ChainId(31337L), "http://hardhat:8545", null),
    AMPNET_POA(ChainId(1984L), "https://poa.ampnet.io/rpc", null);

    companion object {
        private val map = values().associateBy(Chain::id)
        fun fromId(type: ChainId) = map[type]
    }
}
