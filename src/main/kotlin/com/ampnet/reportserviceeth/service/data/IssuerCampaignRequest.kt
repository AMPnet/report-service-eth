package com.ampnet.reportserviceeth.service.data

data class IssuerCampaignRequest(val issuerAddress: String, val campaignAddress: String, val chainId: Long) {
    fun toIssuerRequest() = IssuerRequest(issuerAddress, chainId)
}
