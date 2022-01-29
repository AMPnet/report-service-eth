package com.ampnet.reportserviceeth.blockchain.properties

import com.ampnet.reportserviceeth.config.ChainProperties
import com.ampnet.reportserviceeth.util.ChainId
import org.web3j.protocol.Web3j
import org.web3j.tx.TransactionManager

data class ChainPropertiesWithServices(
    val web3j: Web3j,
    val transactionManager: TransactionManager,
    val chain: ChainProperties,
    val chainId: ChainId
)
