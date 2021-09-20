package com.ampnet.reportserviceeth.service

interface IpfsService {
    fun getLogoUrl(issuerInfoHash: String): String?
}
