package com.ampnet.reportserviceeth.controller.pojo

import com.ampnet.reportserviceeth.util.ChainId
import com.ampnet.reportserviceeth.util.ContractAddress
import com.ampnet.reportserviceeth.util.WalletAddress

data class TransactionsServiceRequest(
    val address: WalletAddress,
    val chainId: ChainId,
    val issuer: ContractAddress,
    val period: PeriodServiceRequest
)
