package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.service.impl.IpfsServiceImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled("Not for automated testing")
class IpfsServiceTest : JpaServiceTestBase() {

    @Test
    fun mustGetLogoHash() {
        val fileService = IpfsServiceImpl(applicationProperties, restTemplate)
        val logoHash = fileService.getLogoUrl("QmQ1wY6jd5uqAcPbdANR6BDqQt8fqEoCc64ypC6dvwnmTb")
        assertThat(logoHash).isNotNull
    }
}
