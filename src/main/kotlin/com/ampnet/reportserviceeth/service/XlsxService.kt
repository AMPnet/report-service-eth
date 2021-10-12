package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.service.data.IssuerCampaignRequest
import com.ampnet.reportserviceeth.service.data.IssuerRequest

interface XlsxService {
    fun generateXlsx(issuerRequest: IssuerRequest): ByteArray
    fun generateXlsx(issuerCampaignRequest: IssuerCampaignRequest): ByteArray
}
