package com.ampnet.reportserviceeth.service

interface FileService {
    fun getLogoHash(issuerInfoHash: String): String?
}
