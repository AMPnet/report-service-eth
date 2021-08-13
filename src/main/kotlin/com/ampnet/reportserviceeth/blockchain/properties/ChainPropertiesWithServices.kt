package com.ampnet.reportserviceeth.blockchain.properties

import org.web3j.protocol.Web3j
import org.web3j.tx.TransactionManager

data class ChainPropertiesWithServices(
    val web3j: Web3j,
    val transactionManager: TransactionManager
)
