package com.ampnet.reportserviceeth.service.data

import com.ampnet.reportserviceeth.util.ChainId
import com.ampnet.reportserviceeth.util.ContractAddress

data class IssuerRequest(val address: ContractAddress, val chainId: ChainId)
