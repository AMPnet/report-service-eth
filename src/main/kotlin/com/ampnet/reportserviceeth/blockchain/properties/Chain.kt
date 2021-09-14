package com.ampnet.reportserviceeth.blockchain.properties

@Suppress("MagicNumber")
enum class Chain(val id: Long, val rpcUrl: String, val infura: String?) {
    MATIC_MAIN(137, "https://polygon-rpc.com/", null),
    MATIC_TESTNET_MUMBAI(80001, "https://matic-mumbai.chainstacklabs.com/", null),
    ETHEREUM_MAIN(1, "https://cloudflare-eth.com/", "https://mainnet.infura.io/v3/"),
    HARDHAT_TESTNET(31337, "http://hardhat:8545", null);

    companion object {
        private val map = values().associateBy(Chain::id)
        fun fromId(type: Long) = map[type]
    }
}
