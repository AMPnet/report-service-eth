package com.ampnet.reportserviceeth.blockchain;

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
 * <p>Generated with web3j version 1.4.1.
 */
@SuppressWarnings("rawtypes")
public class ICampaignCommon extends Contract {
    public static final String BINARY = "";

    public static final String FUNC_CLAIMEDAMOUNT = "claimedAmount";

    public static final String FUNC_COMMONSTATE = "commonState";

    public static final String FUNC_FLAVOR = "flavor";

    public static final String FUNC_INVESTMENTAMOUNT = "investmentAmount";

    public static final String FUNC_TOKENAMOUNT = "tokenAmount";

    public static final String FUNC_VERSION = "version";

    @Deprecated
    protected ICampaignCommon(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ICampaignCommon(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ICampaignCommon(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ICampaignCommon(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<BigInteger> claimedAmount(String investor) {
        final Function function = new Function(FUNC_CLAIMEDAMOUNT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, investor)), 
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

    public RemoteFunctionCall<BigInteger> investmentAmount(String investor) {
        final Function function = new Function(FUNC_INVESTMENTAMOUNT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, investor)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> tokenAmount(String investor) {
        final Function function = new Function(FUNC_TOKENAMOUNT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, investor)), 
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
    public static ICampaignCommon load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ICampaignCommon(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ICampaignCommon load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ICampaignCommon(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ICampaignCommon load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ICampaignCommon(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ICampaignCommon load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ICampaignCommon(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ICampaignCommon> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ICampaignCommon.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ICampaignCommon> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ICampaignCommon.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<ICampaignCommon> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ICampaignCommon.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ICampaignCommon> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ICampaignCommon.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
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
            super(new org.web3j.abi.datatypes.Utf8String(flavor),new org.web3j.abi.datatypes.Utf8String(version),new org.web3j.abi.datatypes.Address(contractAddress),new org.web3j.abi.datatypes.Address(owner),new org.web3j.abi.datatypes.Utf8String(info),new org.web3j.abi.datatypes.Address(asset),new org.web3j.abi.datatypes.Address(stablecoin),new org.web3j.abi.datatypes.generated.Uint256(softCap),new org.web3j.abi.datatypes.Bool(finalized),new org.web3j.abi.datatypes.Bool(canceled),new org.web3j.abi.datatypes.generated.Uint256(pricePerToken),new org.web3j.abi.datatypes.generated.Uint256(fundsRaised),new org.web3j.abi.datatypes.generated.Uint256(tokensSold));
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
}
