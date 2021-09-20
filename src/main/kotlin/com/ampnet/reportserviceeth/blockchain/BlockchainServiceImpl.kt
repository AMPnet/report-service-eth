package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesHandler
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import com.ampnet.reportserviceeth.service.sendSafely
import com.ampnet.reportserviceth.contract.IIssuer
import com.ampnet.reportserviceth.contract.IIssuerCommon
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger
import kotlin.jvm.Throws

private val logger = KotlinLogging.logger {}

@Service
class BlockchainServiceImpl(private val chainPropertiesHandler: ChainPropertiesHandler) : BlockchainService {

    override fun getTransactions(wallet: String, chainId: Long): List<TransactionInfo> {
        logger.debug { "Get transactions for wallet address: $wallet" }
        if (wallet.isBlank()) return emptyList()
        TODO("Not implemented")
    }

    @Throws(InternalException::class)
    override fun getIssuerOwner(issuerRequest: IssuerRequest): String {
        logger.debug { "Get owner of issuer: $issuerRequest" }
        val chainProperties = chainPropertiesHandler.getBlockchainProperties(issuerRequest.chainId)
        val contract = IIssuerCommon.load(
            issuerRequest.address, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        return contract.commonState().sendSafely()?.owner
            ?: throw InternalException(
                ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
                "Failed to fetch issuer owner address for contract address: $issuerRequest"
            )
    }

    @Throws(InternalException::class)
    override fun getWhitelistedAddress(issuerRequest: IssuerRequest): List<String> {
        logger.debug { "Get whitelisted accounts for issuer: $issuerRequest" }
        val chainProperties = chainPropertiesHandler.getBlockchainProperties(issuerRequest.chainId)
        val contract = IIssuer.load(
            issuerRequest.address, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        return contract.walletRecords.sendSafely()
            ?.filterIsInstance<IIssuer.WalletRecord>()
            ?.filter { it.whitelisted }
            ?.map { it.wallet }
            ?: throw InternalException(
                ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
                "Failed to fetch whitelisted addresses for issuer contract address: $issuerRequest"
            )
    }

    @Throws(InternalException::class)
    override fun getBlockNumber(chainId: Long): BigInteger {
        val chainProperties = chainPropertiesHandler.getBlockchainProperties(chainId)
        return chainProperties.web3j.ethBlockNumber().sendSafely()?.blockNumber
            ?: throw InternalException(ErrorCode.INT_JSON_RPC_BLOCKCHAIN, "Failed to fetch latest block number")
    }

    override fun getIssuerCommonState(chainId: Long, issuer: String): IIssuerCommon.IssuerCommonState? {
        val chainProperties = chainPropertiesHandler.getBlockchainProperties(chainId)
        val issuerContract = IIssuerCommon.load(
            issuer, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        return issuerContract.commonState().sendSafely()
    }
}
