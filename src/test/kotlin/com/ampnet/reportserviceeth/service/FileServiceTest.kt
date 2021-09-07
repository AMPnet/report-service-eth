package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.service.impl.FileServiceImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

// @Disabled("Not for automated testing")
class FileServiceTest : JpaServiceTestBase() {

    @Test
    fun mustGetLogoHash() {
        val fileService = FileServiceImpl(restTemplate)
        val logoHash = fileService.getLogoHash("QmQ1wY6jd5uqAcPbdANR6BDqQt8fqEoCc64ypC6dvwnmTb")
        assertThat(logoHash).isNotNull
    }
}
