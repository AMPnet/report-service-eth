package com.ampnet.reportserviceeth.security

import org.springframework.security.test.context.support.WithSecurityContext

@Retention(value = AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@WithSecurityContext(factory = WithMockUserSecurityFactory::class)
annotation class WithMockCrowdfundUser(
    val address: String = "0x8f52B0cC50967fc59C6289f8FDB3E356EdeEBD23"
)
