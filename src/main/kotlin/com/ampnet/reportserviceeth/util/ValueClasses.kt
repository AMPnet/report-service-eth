package com.ampnet.reportserviceeth.util

import java.math.BigInteger

@JvmInline
value class TransactionHash(val value: String)

@JvmInline
value class WalletAddress private constructor(val value: String) {
    companion object {
        operator fun invoke(value: String) = WalletAddress(value.lowercase())
    }
}

@JvmInline
value class ContractAddress private constructor(val value: String) {
    companion object {
        operator fun invoke(value: String) = ContractAddress(value.lowercase())
    }

    fun asWallet() = WalletAddress(value)
}

@JvmInline
value class BlockNumber(val value: BigInteger) {
    constructor(value: Long) : this(BigInteger.valueOf(value))
}

@JvmInline
value class ChainId(val value: Long)
