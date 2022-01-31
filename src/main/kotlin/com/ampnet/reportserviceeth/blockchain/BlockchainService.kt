package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import com.ampnet.reportserviceeth.util.BlockNumber
import com.ampnet.reportserviceeth.util.ChainId
import com.ampnet.reportserviceeth.util.ContractAddress
import com.ampnet.reportserviceeth.util.WalletAddress
import com.ampnet.reportserviceth.contract.IIssuerCommon

interface BlockchainService {
    fun getTransactions(wallet: WalletAddress, chainId: ChainId): List<TransactionInfo>

    @Throws(InternalException::class)
    fun getIssuerOwner(issuerRequest: IssuerRequest): WalletAddress

    @Throws(InternalException::class)
    fun getWhitelistedAddress(issuerRequest: IssuerRequest): List<WalletAddress>

    @Throws(InternalException::class)
    fun getBlockNumber(chainId: ChainId): BlockNumber

    fun getIssuerCommonState(chainId: ChainId, issuer: ContractAddress): IIssuerCommon.IssuerCommonState?
}
