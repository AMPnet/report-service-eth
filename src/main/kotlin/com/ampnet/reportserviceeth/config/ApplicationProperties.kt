package com.ampnet.reportserviceeth.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.math.BigInteger

@Configuration
@ConfigurationProperties(prefix = "com.ampnet.reportserviceeth")
class ApplicationProperties {
    val jwt: JwtProperties = JwtProperties()
    var grpc: GrpcProperties = GrpcProperties()
    val queue = QueueProperties()
    val chainEthereum = ChainProperties()
    val chainGoerli = ChainProperties()
    val chainMatic = ChainProperties()
    val chainMumbai = ChainProperties()
    val chainHardhatTestnet = ChainProperties()
    val chainPoa = ChainProperties()
    val ipfs = IpfsProperties()
    lateinit var infuraId: String
}

class JwtProperties {
    lateinit var publicKey: String
}

@Suppress("MagicNumber")
class GrpcProperties {
    var identityServiceTimeout: Long = 10000
}

@Suppress("MagicNumber")
class QueueProperties {
    var polling: Long = 5 * 1000
    var initialDelay: Long = 15_000
}

@Suppress("MagicNumber")
class ChainProperties {
    var callerAddress: String = "0x0000000000000000000000000000000000000000"
    var cfManagerFactoryAddresses: List<String> = emptyList()
    var payoutManagerAddresses: List<String> = emptyList()
    var startBlockNumber: BigInteger = BigInteger.ONE
    var numOfConfirmations: BigInteger = BigInteger.TEN
    var maxBlocks: BigInteger = BigInteger.valueOf(1000L)
}

class IpfsProperties {
    var frontendApi: String? = null
    var ipfsUrl = "https://ampnet.mypinata.cloud/ipfs/"
}
