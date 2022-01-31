package com.ampnet.reportserviceeth.controller.pojo

import com.ampnet.reportserviceeth.util.ChainId
import com.ampnet.reportserviceeth.util.ContractAddress
import com.ampnet.reportserviceeth.util.TransactionHash
import com.ampnet.reportserviceeth.util.WalletAddress

data class TransactionServiceRequest(
    val address: WalletAddress,
    val txHash: TransactionHash,
    val chainId: ChainId,
    val issuer: ContractAddress
)
