package com.ampnet.reportserviceth.contract;

import java.math.BigInteger;
import java.util.Arrays;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.8.7.
 */
@SuppressWarnings("rawtypes")
public class IAsset extends Contract {
    public static final String BINARY = "";

    public static final String FUNC_GETSTATE = "getState";

    @Deprecated
    protected IAsset(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected IAsset(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected IAsset(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected IAsset(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<AssetState> getState() {
        final Function function = new Function(FUNC_GETSTATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<AssetState>() {}));
        return executeRemoteCallSingleValueReturn(function, AssetState.class);
    }

    @Deprecated
    public static IAsset load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new IAsset(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static IAsset load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new IAsset(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static IAsset load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new IAsset(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static IAsset load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new IAsset(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<IAsset> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IAsset.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<IAsset> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(IAsset.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<IAsset> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IAsset.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<IAsset> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(IAsset.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class AssetState extends DynamicStruct {
        public String flavor;

        public String version;

        public String contractAddress;

        public String owner;

        public BigInteger initialTokenSupply;

        public Boolean transferable;

        public Boolean whitelistRequiredForRevenueClaim;

        public Boolean whitelistRequiredForLiquidationClaim;

        public Boolean assetApprovedByIssuer;

        public String issuer;

        public String apxRegistry;

        public String info;

        public String name;

        public String symbol;

        public BigInteger totalAmountRaised;

        public BigInteger totalTokensSold;

        public BigInteger highestTokenSellPrice;

        public BigInteger totalTokensLocked;

        public BigInteger totalTokensLockedAndLiquidated;

        public Boolean liquidated;

        public BigInteger liquidationFundsTotal;

        public BigInteger liquidationTimestamp;

        public BigInteger liquidationFundsClaimed;

        public AssetState(String flavor, String version, String contractAddress, String owner, BigInteger initialTokenSupply, Boolean transferable, Boolean whitelistRequiredForRevenueClaim, Boolean whitelistRequiredForLiquidationClaim, Boolean assetApprovedByIssuer, String issuer, String apxRegistry, String info, String name, String symbol, BigInteger totalAmountRaised, BigInteger totalTokensSold, BigInteger highestTokenSellPrice, BigInteger totalTokensLocked, BigInteger totalTokensLockedAndLiquidated, Boolean liquidated, BigInteger liquidationFundsTotal, BigInteger liquidationTimestamp, BigInteger liquidationFundsClaimed) {
            super(new Utf8String(flavor),new Utf8String(version),new Address(contractAddress),new Address(owner),new Uint256(initialTokenSupply),new Bool(transferable),new Bool(whitelistRequiredForRevenueClaim),new Bool(whitelistRequiredForLiquidationClaim),new Bool(assetApprovedByIssuer),new Address(issuer),new Address(apxRegistry),new Utf8String(info),new Utf8String(name),new Utf8String(symbol),new Uint256(totalAmountRaised),new Uint256(totalTokensSold),new Uint256(highestTokenSellPrice),new Uint256(totalTokensLocked),new Uint256(totalTokensLockedAndLiquidated),new Bool(liquidated),new Uint256(liquidationFundsTotal),new Uint256(liquidationTimestamp),new Uint256(liquidationFundsClaimed));
            this.flavor = flavor;
            this.version = version;
            this.contractAddress = contractAddress;
            this.owner = owner;
            this.initialTokenSupply = initialTokenSupply;
            this.transferable = transferable;
            this.whitelistRequiredForRevenueClaim = whitelistRequiredForRevenueClaim;
            this.whitelistRequiredForLiquidationClaim = whitelistRequiredForLiquidationClaim;
            this.assetApprovedByIssuer = assetApprovedByIssuer;
            this.issuer = issuer;
            this.apxRegistry = apxRegistry;
            this.info = info;
            this.name = name;
            this.symbol = symbol;
            this.totalAmountRaised = totalAmountRaised;
            this.totalTokensSold = totalTokensSold;
            this.highestTokenSellPrice = highestTokenSellPrice;
            this.totalTokensLocked = totalTokensLocked;
            this.totalTokensLockedAndLiquidated = totalTokensLockedAndLiquidated;
            this.liquidated = liquidated;
            this.liquidationFundsTotal = liquidationFundsTotal;
            this.liquidationTimestamp = liquidationTimestamp;
            this.liquidationFundsClaimed = liquidationFundsClaimed;
        }

        public AssetState(Utf8String flavor, Utf8String version, Address contractAddress, Address owner, Uint256 initialTokenSupply, Bool transferable, Bool whitelistRequiredForRevenueClaim, Bool whitelistRequiredForLiquidationClaim, Bool assetApprovedByIssuer, Address issuer, Address apxRegistry, Utf8String info, Utf8String name, Utf8String symbol, Uint256 totalAmountRaised, Uint256 totalTokensSold, Uint256 highestTokenSellPrice, Uint256 totalTokensLocked, Uint256 totalTokensLockedAndLiquidated, Bool liquidated, Uint256 liquidationFundsTotal, Uint256 liquidationTimestamp, Uint256 liquidationFundsClaimed) {
            super(flavor,version,contractAddress,owner,initialTokenSupply,transferable,whitelistRequiredForRevenueClaim,whitelistRequiredForLiquidationClaim,assetApprovedByIssuer,issuer,apxRegistry,info,name,symbol,totalAmountRaised,totalTokensSold,highestTokenSellPrice,totalTokensLocked,totalTokensLockedAndLiquidated,liquidated,liquidationFundsTotal,liquidationTimestamp,liquidationFundsClaimed);
            this.flavor = flavor.getValue();
            this.version = version.getValue();
            this.contractAddress = contractAddress.getValue();
            this.owner = owner.getValue();
            this.initialTokenSupply = initialTokenSupply.getValue();
            this.transferable = transferable.getValue();
            this.whitelistRequiredForRevenueClaim = whitelistRequiredForRevenueClaim.getValue();
            this.whitelistRequiredForLiquidationClaim = whitelistRequiredForLiquidationClaim.getValue();
            this.assetApprovedByIssuer = assetApprovedByIssuer.getValue();
            this.issuer = issuer.getValue();
            this.apxRegistry = apxRegistry.getValue();
            this.info = info.getValue();
            this.name = name.getValue();
            this.symbol = symbol.getValue();
            this.totalAmountRaised = totalAmountRaised.getValue();
            this.totalTokensSold = totalTokensSold.getValue();
            this.highestTokenSellPrice = highestTokenSellPrice.getValue();
            this.totalTokensLocked = totalTokensLocked.getValue();
            this.totalTokensLockedAndLiquidated = totalTokensLockedAndLiquidated.getValue();
            this.liquidated = liquidated.getValue();
            this.liquidationFundsTotal = liquidationFundsTotal.getValue();
            this.liquidationTimestamp = liquidationTimestamp.getValue();
            this.liquidationFundsClaimed = liquidationFundsClaimed.getValue();
        }
    }
}
