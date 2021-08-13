package com.ampnet.reportserviceeth.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "com.ampnet.reportserviceeth")
class ApplicationProperties {
    val jwt: JwtProperties = JwtProperties()
    var grpc: GrpcProperties = GrpcProperties()
    val smartContract = SmartContractProperties()
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
}
