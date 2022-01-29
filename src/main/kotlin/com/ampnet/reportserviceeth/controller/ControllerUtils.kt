package com.ampnet.reportserviceeth.controller

import com.ampnet.core.jwt.exception.TokenException
import com.ampnet.reportserviceeth.util.WalletAddress
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder

internal object ControllerUtils {
    fun getAddressFromSecurityContext(): WalletAddress =
        (SecurityContextHolder.getContext().authentication.principal as? String)?.let { WalletAddress(it) }
            ?: throw TokenException("SecurityContext authentication principal must be String")

    fun getHttpHeadersForPdf(): HttpHeaders =
        HttpHeaders().apply { contentType = MediaType.APPLICATION_PDF }
}
