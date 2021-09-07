package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import java.math.BigInteger

interface BlockchainService {
    fun getTransactions(wallet: String, chainId: Long): List<TransactionInfo>

    @Throws(InternalException::class)
    fun getIssuerOwner(issuerRequest: IssuerRequest): String

    @Throws(InternalException::class)
    fun getWhitelistedAddress(issuerRequest: IssuerRequest): List<String>

    @Throws(InternalException::class)
    fun getBlockNumber(chainId: Long): BigInteger

    fun getIssuerState(chainId: Long, issuer: String): IIssuer.IssuerState?
}
