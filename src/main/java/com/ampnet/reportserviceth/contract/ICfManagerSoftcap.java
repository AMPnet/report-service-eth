package com.ampnet.reportserviceth.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
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
public class ICfManagerSoftcap extends Contract {
    public static final String BINARY = "";

    public static final String FUNC_CHANGEOWNERSHIP = "changeOwnership";

    public static final String FUNC_CLAIMEDAMOUNT = "claimedAmount";

    public static final String FUNC_COMMONSTATE = "commonState";

    public static final String FUNC_FLAVOR = "flavor";

    public static final String FUNC_GETINFOHISTORY = "getInfoHistory";

    public static final String FUNC_GETSTATE = "getState";

    public static final String FUNC_INVESTMENTAMOUNT = "investmentAmount";

    public static final String FUNC_TOKENAMOUNT = "tokenAmount";

    public static final String FUNC_VERSION = "version";

    @Deprecated
    protected ICfManagerSoftcap(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ICfManagerSoftcap(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ICfManagerSoftcap(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ICfManagerSoftcap(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> changeOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_CHANGEOWNERSHIP, 
                Arrays.<Type>asList(new Address(160, newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> claimedAmount(String investor) {
        final Function function = new Function(FUNC_CLAIMEDAMOUNT, 
                Arrays.<Type>asList(new Address(160, investor)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<CampaignCommonState> commonState() {
        final Function function = new Function(FUNC_COMMONSTATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<CampaignCommonState>() {}));
        return executeRemoteCallSingleValueReturn(function, CampaignCommonState.class);
    }

    public RemoteFunctionCall<String> flavor() {
        final Function function = new Function(FUNC_FLAVOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<List> getInfoHistory() {
        final Function function = new Function(FUNC_GETINFOHISTORY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<InfoEntry>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<CfManagerSoftcapState> getState() {
        final Function function = new Function(FUNC_GETSTATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<CfManagerSoftcapState>() {}));
        return executeRemoteCallSingleValueReturn(function, CfManagerSoftcapState.class);
    }

    public RemoteFunctionCall<BigInteger> investmentAmount(String investor) {
        final Function function = new Function(FUNC_INVESTMENTAMOUNT, 
                Arrays.<Type>asList(new Address(160, investor)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> tokenAmount(String investor) {
        final Function function = new Function(FUNC_TOKENAMOUNT, 
                Arrays.<Type>asList(new Address(160, investor)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> version() {
        final Function function = new Function(FUNC_VERSION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Deprecated
    public static ICfManagerSoftcap load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ICfManagerSoftcap(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ICfManagerSoftcap load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ICfManagerSoftcap(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ICfManagerSoftcap load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ICfManagerSoftcap(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ICfManagerSoftcap load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ICfManagerSoftcap(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ICfManagerSoftcap> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ICfManagerSoftcap.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ICfManagerSoftcap> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ICfManagerSoftcap.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<ICfManagerSoftcap> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ICfManagerSoftcap.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ICfManagerSoftcap> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ICfManagerSoftcap.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class CampaignCommonState extends DynamicStruct {
        public String flavor;

        public String version;

        public String contractAddress;

        public String owner;

        public String info;

        public String asset;

        public String stablecoin;

        public BigInteger softCap;

        public Boolean finalized;

        public Boolean canceled;

        public BigInteger pricePerToken;

        public BigInteger fundsRaised;

        public BigInteger tokensSold;

        public CampaignCommonState(String flavor, String version, String contractAddress, String owner, String info, String asset, String stablecoin, BigInteger softCap, Boolean finalized, Boolean canceled, BigInteger pricePerToken, BigInteger fundsRaised, BigInteger tokensSold) {
            super(new Utf8String(flavor),new Utf8String(version),new Address(contractAddress),new Address(owner),new Utf8String(info),new Address(asset),new Address(stablecoin),new Uint256(softCap),new Bool(finalized),new Bool(canceled),new Uint256(pricePerToken),new Uint256(fundsRaised),new Uint256(tokensSold));
            this.flavor = flavor;
            this.version = version;
            this.contractAddress = contractAddress;
            this.owner = owner;
            this.info = info;
            this.asset = asset;
            this.stablecoin = stablecoin;
            this.softCap = softCap;
            this.finalized = finalized;
            this.canceled = canceled;
            this.pricePerToken = pricePerToken;
            this.fundsRaised = fundsRaised;
            this.tokensSold = tokensSold;
        }

        public CampaignCommonState(Utf8String flavor, Utf8String version, Address contractAddress, Address owner, Utf8String info, Address asset, Address stablecoin, Uint256 softCap, Bool finalized, Bool canceled, Uint256 pricePerToken, Uint256 fundsRaised, Uint256 tokensSold) {
            super(flavor,version,contractAddress,owner,info,asset,stablecoin,softCap,finalized,canceled,pricePerToken,fundsRaised,tokensSold);
            this.flavor = flavor.getValue();
            this.version = version.getValue();
            this.contractAddress = contractAddress.getValue();
            this.owner = owner.getValue();
            this.info = info.getValue();
            this.asset = asset.getValue();
            this.stablecoin = stablecoin.getValue();
            this.softCap = softCap.getValue();
            this.finalized = finalized.getValue();
            this.canceled = canceled.getValue();
            this.pricePerToken = pricePerToken.getValue();
            this.fundsRaised = fundsRaised.getValue();
            this.tokensSold = tokensSold.getValue();
        }
    }

    public static class InfoEntry extends DynamicStruct {
        public String info;

        public BigInteger timestamp;

        public InfoEntry(String info, BigInteger timestamp) {
            super(new Utf8String(info),new Uint256(timestamp));
            this.info = info;
            this.timestamp = timestamp;
        }

        public InfoEntry(Utf8String info, Uint256 timestamp) {
            super(info,timestamp);
            this.info = info.getValue();
            this.timestamp = timestamp.getValue();
        }
    }

    public static class CfManagerSoftcapState extends DynamicStruct {
        public String flavor;

        public String version;

        public String contractAddress;

        public String owner;

        public String asset;

        public String issuer;

        public String stablecoin;

        public BigInteger tokenPrice;

        public BigInteger softCap;

        public BigInteger minInvestment;

        public BigInteger maxInvestment;

        public Boolean whitelistRequired;

        public Boolean finalized;

        public Boolean canceled;

        public BigInteger totalClaimableTokens;

        public BigInteger totalInvestorsCount;

        public BigInteger totalClaimsCount;

        public BigInteger totalFundsRaised;

        public BigInteger totalTokensSold;

        public BigInteger totalTokensBalance;

        public String info;

        public CfManagerSoftcapState(String flavor, String version, String contractAddress, String owner, String asset, String issuer, String stablecoin, BigInteger tokenPrice, BigInteger softCap, BigInteger minInvestment, BigInteger maxInvestment, Boolean whitelistRequired, Boolean finalized, Boolean canceled, BigInteger totalClaimableTokens, BigInteger totalInvestorsCount, BigInteger totalClaimsCount, BigInteger totalFundsRaised, BigInteger totalTokensSold, BigInteger totalTokensBalance, String info) {
            super(new Utf8String(flavor),new Utf8String(version),new Address(contractAddress),new Address(owner),new Address(asset),new Address(issuer),new Address(stablecoin),new Uint256(tokenPrice),new Uint256(softCap),new Uint256(minInvestment),new Uint256(maxInvestment),new Bool(whitelistRequired),new Bool(finalized),new Bool(canceled),new Uint256(totalClaimableTokens),new Uint256(totalInvestorsCount),new Uint256(totalClaimsCount),new Uint256(totalFundsRaised),new Uint256(totalTokensSold),new Uint256(totalTokensBalance),new Utf8String(info));
            this.flavor = flavor;
            this.version = version;
            this.contractAddress = contractAddress;
            this.owner = owner;
            this.asset = asset;
            this.issuer = issuer;
            this.stablecoin = stablecoin;
            this.tokenPrice = tokenPrice;
            this.softCap = softCap;
            this.minInvestment = minInvestment;
            this.maxInvestment = maxInvestment;
            this.whitelistRequired = whitelistRequired;
            this.finalized = finalized;
            this.canceled = canceled;
            this.totalClaimableTokens = totalClaimableTokens;
            this.totalInvestorsCount = totalInvestorsCount;
            this.totalClaimsCount = totalClaimsCount;
            this.totalFundsRaised = totalFundsRaised;
            this.totalTokensSold = totalTokensSold;
            this.totalTokensBalance = totalTokensBalance;
            this.info = info;
        }

        public CfManagerSoftcapState(Utf8String flavor, Utf8String version, Address contractAddress, Address owner, Address asset, Address issuer, Address stablecoin, Uint256 tokenPrice, Uint256 softCap, Uint256 minInvestment, Uint256 maxInvestment, Bool whitelistRequired, Bool finalized, Bool canceled, Uint256 totalClaimableTokens, Uint256 totalInvestorsCount, Uint256 totalClaimsCount, Uint256 totalFundsRaised, Uint256 totalTokensSold, Uint256 totalTokensBalance, Utf8String info) {
            super(flavor,version,contractAddress,owner,asset,issuer,stablecoin,tokenPrice,softCap,minInvestment,maxInvestment,whitelistRequired,finalized,canceled,totalClaimableTokens,totalInvestorsCount,totalClaimsCount,totalFundsRaised,totalTokensSold,totalTokensBalance,info);
            this.flavor = flavor.getValue();
            this.version = version.getValue();
            this.contractAddress = contractAddress.getValue();
            this.owner = owner.getValue();
            this.asset = asset.getValue();
            this.issuer = issuer.getValue();
            this.stablecoin = stablecoin.getValue();
            this.tokenPrice = tokenPrice.getValue();
            this.softCap = softCap.getValue();
            this.minInvestment = minInvestment.getValue();
            this.maxInvestment = maxInvestment.getValue();
            this.whitelistRequired = whitelistRequired.getValue();
            this.finalized = finalized.getValue();
            this.canceled = canceled.getValue();
            this.totalClaimableTokens = totalClaimableTokens.getValue();
            this.totalInvestorsCount = totalInvestorsCount.getValue();
            this.totalClaimsCount = totalClaimsCount.getValue();
            this.totalFundsRaised = totalFundsRaised.getValue();
            this.totalTokensSold = totalTokensSold.getValue();
            this.totalTokensBalance = totalTokensBalance.getValue();
            this.info = info.getValue();
        }
    }
}
