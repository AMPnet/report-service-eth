package com.ampnet.reportserviceeth.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

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
    var snapshotDistributorAddresses: List<String> = emptyList()
    var startBlockNumber: Long = 1
    var numOfConfirmations: Long = 10
    var maxBlocks: Long = 1000
}

class IpfsProperties {
    var frontendApi: String? = null
    var ipfsUrl = "https://ampnet.mypinata.cloud/ipfs/"
}
