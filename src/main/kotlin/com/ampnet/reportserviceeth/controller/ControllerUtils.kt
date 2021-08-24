package com.ampnet.reportserviceeth.controller

import com.ampnet.core.jwt.exception.TokenException
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder

internal object ControllerUtils {
    fun getAddressFromSecurityContext(): String =
        SecurityContextHolder.getContext().authentication.principal as? String
            ?: throw TokenException("SecurityContext authentication principal must be String")

    fun getHttpHeadersForPdf(): HttpHeaders =
        HttpHeaders().apply { contentType = MediaType.APPLICATION_PDF }
}
