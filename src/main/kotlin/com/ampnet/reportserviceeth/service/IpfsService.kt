package com.ampnet.reportserviceeth.service

interface IpfsService {
    fun getLogoHash(issuerInfoHash: String): String?
}
