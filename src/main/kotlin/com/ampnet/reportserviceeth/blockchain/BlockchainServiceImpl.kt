package com.ampnet.reportserviceeth.blockchain

import com.ampnet.reportserviceeth.blockchain.properties.ChainPropertiesHandler
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import com.ampnet.reportserviceeth.service.sendSafely
import com.ampnet.reportserviceeth.util.BlockNumber
import com.ampnet.reportserviceeth.util.ChainId
import com.ampnet.reportserviceeth.util.ContractAddress
import com.ampnet.reportserviceeth.util.WalletAddress
import com.ampnet.reportserviceth.contract.IIssuer
import com.ampnet.reportserviceth.contract.IIssuerCommon
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.web3j.tx.gas.DefaultGasProvider

private val logger = KotlinLogging.logger {}

@Service
class BlockchainServiceImpl(private val chainPropertiesHandler: ChainPropertiesHandler) : BlockchainService {

    override fun getTransactions(wallet: WalletAddress, chainId: ChainId): List<TransactionInfo> {
        logger.debug { "Get transactions for wallet address: $wallet on chainId: $chainId" }
        if (wallet.value.isBlank()) return emptyList()
        TODO("Not implemented")
    }

    @Throws(InternalException::class)
    override fun getIssuerOwner(issuerRequest: IssuerRequest): WalletAddress {
        logger.debug { "Get owner of issuer: $issuerRequest on chainId: ${issuerRequest.chainId}" }
        val chainProperties = chainPropertiesHandler.getBlockchainProperties(issuerRequest.chainId)
        val contract = IIssuerCommon.load(
            issuerRequest.address.value, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        return contract.commonState().sendSafely()?.owner?.let { WalletAddress(it) }
            ?: throw InternalException(
                ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
                "Failed to fetch issuer owner address for contract address: $issuerRequest"
            )
    }

    @Throws(InternalException::class)
    override fun getWhitelistedAddress(issuerRequest: IssuerRequest): List<WalletAddress> {
        logger.debug { "Get whitelisted accounts for issuer: $issuerRequest on chainId: ${issuerRequest.chainId}" }
        val chainProperties = chainPropertiesHandler.getBlockchainProperties(issuerRequest.chainId)
        val contract = IIssuer.load(
            issuerRequest.address.value, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        return contract.walletRecords.sendSafely()
            ?.filterIsInstance<IIssuer.WalletRecord>()
            ?.filter { it.whitelisted }
            ?.map { WalletAddress(it.wallet) }
            ?: throw InternalException(
                ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
                "Failed to fetch whitelisted addresses for issuer contract address: $issuerRequest " +
                    "on chainId: ${issuerRequest.chainId}"
            )
    }

    @Throws(InternalException::class)
    override fun getBlockNumber(chainId: ChainId): BlockNumber {
        val chainProperties = chainPropertiesHandler.getBlockchainProperties(chainId)
        return chainProperties.web3j.ethBlockNumber().sendSafely()?.blockNumber?.let { BlockNumber(it) }
            ?: throw InternalException(
                ErrorCode.INT_JSON_RPC_BLOCKCHAIN,
                "Failed to fetch latest block number on chainId: $chainId"
            )
    }

    override fun getIssuerCommonState(chainId: ChainId, issuer: ContractAddress): IIssuerCommon.IssuerCommonState? {
        val chainProperties = chainPropertiesHandler.getBlockchainProperties(chainId)
        val issuerContract = IIssuerCommon.load(
            issuer.value, chainProperties.web3j, chainProperties.transactionManager, DefaultGasProvider()
        )
        return issuerContract.commonState().sendSafely()
    }
}
