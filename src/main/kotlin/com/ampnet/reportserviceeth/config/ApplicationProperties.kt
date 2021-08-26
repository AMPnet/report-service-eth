package com.ampnet.reportserviceeth.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "com.ampnet.reportserviceeth")
class ApplicationProperties {
    val jwt: JwtProperties = JwtProperties()
    var grpc: GrpcProperties = GrpcProperties()
    val smartContract = SmartContractProperties()
    val queue = QueueProperties()
    lateinit var infuraId: String
}

class JwtProperties {
    lateinit var publicKey: String
}

@Suppress("MagicNumber")
class GrpcProperties {
    var identityServiceTimeout: Long = 10000
}

class SmartContractProperties {
    lateinit var walletAddress: String
    var cfManagerFactoryAddress: String = "0xa77d68e73752b4e4E6670032400f9a1Da522ed22"
    var payoutManagerFactoryAddress: String = "0x39d13eA4781F4FA57a347F5C49dD716048822F16"
}

@Suppress("MagicNumber")
class QueueProperties {
    var polling: Long = 3 * 60 * 1000
    var initialDelay: Long = 15_000
    var startBlockNumber: Long = 18_306_544
    var numOfConfirmations: Long = 10
    var maxBlocks: Long = 1000
}
