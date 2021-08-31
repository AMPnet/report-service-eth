/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.8.4.
 */
@SuppressWarnings("rawtypes")
public class ICfManagerSoftcap extends org.web3j.tx.Contract {
    public static final String BINARY = "";

    public static final String FUNC_GETSTATE = "getState";

    protected ICfManagerSoftcap(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    protected ICfManagerSoftcap(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public org.web3j.protocol.core.RemoteFunctionCall<ICfManagerSoftcap.CfManagerSoftcapState> getState() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETSTATE,
                java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(),
                java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<ICfManagerSoftcap.CfManagerSoftcapState>() {}));
        return executeRemoteCallSingleValueReturn(function, ICfManagerSoftcap.CfManagerSoftcapState.class);
    }

    public static ICfManagerSoftcap load(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return new ICfManagerSoftcap(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ICfManagerSoftcap load(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return new ICfManagerSoftcap(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static org.web3j.protocol.core.RemoteCall<ICfManagerSoftcap> deploy(org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ICfManagerSoftcap.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static org.web3j.protocol.core.RemoteCall<ICfManagerSoftcap> deploy(org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ICfManagerSoftcap.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    public static class CfManagerSoftcapState extends org.web3j.abi.datatypes.DynamicStruct {
        public java.math.BigInteger id;

        public String contractAddress;

        public String ansName;

        public java.math.BigInteger ansId;

        public String createdBy;

        public String owner;

        public String asset;

        public String assetFactory;

        public String issuer;

        public java.math.BigInteger tokenPrice;

        public java.math.BigInteger softCap;

        public java.math.BigInteger minInvestment;

        public java.math.BigInteger maxInvestment;

        public Boolean whitelistRequired;

        public Boolean finalized;

        public Boolean cancelled;

        public java.math.BigInteger totalClaimableTokens;

        public java.math.BigInteger totalInvestorsCount;

        public java.math.BigInteger totalClaimsCount;

        public java.math.BigInteger totalFundsRaised;

        public java.math.BigInteger totalTokensSold;

        public java.math.BigInteger totalTokensBalance;

        public String info;

        public CfManagerSoftcapState(java.math.BigInteger id, String contractAddress, String ansName, java.math.BigInteger ansId, String createdBy, String owner, String asset, String assetFactory, String issuer, java.math.BigInteger tokenPrice, java.math.BigInteger softCap, java.math.BigInteger minInvestment, java.math.BigInteger maxInvestment, Boolean whitelistRequired, Boolean finalized, Boolean cancelled, java.math.BigInteger totalClaimableTokens, java.math.BigInteger totalInvestorsCount, java.math.BigInteger totalClaimsCount, java.math.BigInteger totalFundsRaised, java.math.BigInteger totalTokensSold, java.math.BigInteger totalTokensBalance, String info) {
            super(new org.web3j.abi.datatypes.generated.Uint256(id),new org.web3j.abi.datatypes.Address(contractAddress),new org.web3j.abi.datatypes.Utf8String(ansName),new org.web3j.abi.datatypes.generated.Uint256(ansId),new org.web3j.abi.datatypes.Address(createdBy),new org.web3j.abi.datatypes.Address(owner),new org.web3j.abi.datatypes.Address(asset),new org.web3j.abi.datatypes.Address(assetFactory),new org.web3j.abi.datatypes.Address(issuer),new org.web3j.abi.datatypes.generated.Uint256(tokenPrice),new org.web3j.abi.datatypes.generated.Uint256(softCap),new org.web3j.abi.datatypes.generated.Uint256(minInvestment),new org.web3j.abi.datatypes.generated.Uint256(maxInvestment),new org.web3j.abi.datatypes.Bool(whitelistRequired),new org.web3j.abi.datatypes.Bool(finalized),new org.web3j.abi.datatypes.Bool(cancelled),new org.web3j.abi.datatypes.generated.Uint256(totalClaimableTokens),new org.web3j.abi.datatypes.generated.Uint256(totalInvestorsCount),new org.web3j.abi.datatypes.generated.Uint256(totalClaimsCount),new org.web3j.abi.datatypes.generated.Uint256(totalFundsRaised),new org.web3j.abi.datatypes.generated.Uint256(totalTokensSold),new org.web3j.abi.datatypes.generated.Uint256(totalTokensBalance),new org.web3j.abi.datatypes.Utf8String(info));
            this.id = id;
            this.contractAddress = contractAddress;
            this.ansName = ansName;
            this.ansId = ansId;
            this.createdBy = createdBy;
            this.owner = owner;
            this.asset = asset;
            this.assetFactory = assetFactory;
            this.issuer = issuer;
            this.tokenPrice = tokenPrice;
            this.softCap = softCap;
            this.minInvestment = minInvestment;
            this.maxInvestment = maxInvestment;
            this.whitelistRequired = whitelistRequired;
            this.finalized = finalized;
            this.cancelled = cancelled;
            this.totalClaimableTokens = totalClaimableTokens;
            this.totalInvestorsCount = totalInvestorsCount;
            this.totalClaimsCount = totalClaimsCount;
            this.totalFundsRaised = totalFundsRaised;
            this.totalTokensSold = totalTokensSold;
            this.totalTokensBalance = totalTokensBalance;
            this.info = info;
        }

        public CfManagerSoftcapState(org.web3j.abi.datatypes.generated.Uint256 id, org.web3j.abi.datatypes.Address contractAddress, org.web3j.abi.datatypes.Utf8String ansName, org.web3j.abi.datatypes.generated.Uint256 ansId, org.web3j.abi.datatypes.Address createdBy, org.web3j.abi.datatypes.Address owner, org.web3j.abi.datatypes.Address asset, org.web3j.abi.datatypes.Address assetFactory, org.web3j.abi.datatypes.Address issuer, org.web3j.abi.datatypes.generated.Uint256 tokenPrice, org.web3j.abi.datatypes.generated.Uint256 softCap, org.web3j.abi.datatypes.generated.Uint256 minInvestment, org.web3j.abi.datatypes.generated.Uint256 maxInvestment, org.web3j.abi.datatypes.Bool whitelistRequired, org.web3j.abi.datatypes.Bool finalized, org.web3j.abi.datatypes.Bool cancelled, org.web3j.abi.datatypes.generated.Uint256 totalClaimableTokens, org.web3j.abi.datatypes.generated.Uint256 totalInvestorsCount, org.web3j.abi.datatypes.generated.Uint256 totalClaimsCount, org.web3j.abi.datatypes.generated.Uint256 totalFundsRaised, org.web3j.abi.datatypes.generated.Uint256 totalTokensSold, org.web3j.abi.datatypes.generated.Uint256 totalTokensBalance, org.web3j.abi.datatypes.Utf8String info) {
            super(id,contractAddress,ansName,ansId,createdBy,owner,asset,assetFactory,issuer,tokenPrice,softCap,minInvestment,maxInvestment,whitelistRequired,finalized,cancelled,totalClaimableTokens,totalInvestorsCount,totalClaimsCount,totalFundsRaised,totalTokensSold,totalTokensBalance,info);
            this.id = id.getValue();
            this.contractAddress = contractAddress.getValue();
            this.ansName = ansName.getValue();
            this.ansId = ansId.getValue();
            this.createdBy = createdBy.getValue();
            this.owner = owner.getValue();
            this.asset = asset.getValue();
            this.assetFactory = assetFactory.getValue();
            this.issuer = issuer.getValue();
            this.tokenPrice = tokenPrice.getValue();
            this.softCap = softCap.getValue();
            this.minInvestment = minInvestment.getValue();
            this.maxInvestment = maxInvestment.getValue();
            this.whitelistRequired = whitelistRequired.getValue();
            this.finalized = finalized.getValue();
            this.cancelled = cancelled.getValue();
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
