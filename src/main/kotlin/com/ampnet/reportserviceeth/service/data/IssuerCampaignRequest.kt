package com.ampnet.reportserviceeth.service.data

import com.ampnet.reportserviceeth.util.ChainId
import com.ampnet.reportserviceeth.util.ContractAddress

data class IssuerCampaignRequest(
    val issuerAddress: ContractAddress,
    val campaignAddress: ContractAddress,
    val chainId: ChainId
) {
    fun toIssuerRequest() = IssuerRequest(issuerAddress, chainId)
}
