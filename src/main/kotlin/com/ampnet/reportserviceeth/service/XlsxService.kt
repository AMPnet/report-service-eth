package com.ampnet.reportserviceeth.service

interface XlsxService {
    fun generateXlsx(issuer: String): ByteArray
}
