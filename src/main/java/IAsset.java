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
public class IAsset extends org.web3j.tx.Contract {
    public static final String BINARY = "";

    public static final String FUNC_GETSTATE = "getState";

    protected IAsset(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    protected IAsset(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public org.web3j.protocol.core.RemoteFunctionCall<IAsset.AssetState> getState() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETSTATE,
                java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(),
                java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<IAsset.AssetState>() {}));
        return executeRemoteCallSingleValueReturn(function, IAsset.AssetState.class);
    }

    public static IAsset load(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return new IAsset(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static IAsset load(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return new IAsset(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static org.web3j.protocol.core.RemoteCall<IAsset> deploy(org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IAsset.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static org.web3j.protocol.core.RemoteCall<IAsset> deploy(org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IAsset.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    public static class AssetState extends org.web3j.abi.datatypes.DynamicStruct {
        public java.math.BigInteger id;

        public String contractAddress;

        public String ansName;

        public java.math.BigInteger ansId;

        public String createdBy;

        public String owner;

        public java.math.BigInteger initialTokenSupply;

        public Boolean whitelistRequiredForRevenueClaim;

        public Boolean whitelistRequiredForLiquidationClaim;

        public Boolean assetApprovedByIssuer;

        public String issuer;

        public String apxRegistry;

        public String info;

        public String name;

        public String symbol;

        public java.math.BigInteger totalAmountRaised;

        public java.math.BigInteger totalTokensSold;

        public java.math.BigInteger highestTokenSellPrice;

        public java.math.BigInteger totalTokensLocked;

        public java.math.BigInteger totalTokensLockedAndLiquidated;

        public Boolean liquidated;

        public java.math.BigInteger liquidationFundsTotal;

        public java.math.BigInteger liquidationTimestamp;

        public java.math.BigInteger liquidationFundsClaimed;

        public AssetState(java.math.BigInteger id, String contractAddress, String ansName, java.math.BigInteger ansId, String createdBy, String owner, java.math.BigInteger initialTokenSupply, Boolean whitelistRequiredForRevenueClaim, Boolean whitelistRequiredForLiquidationClaim, Boolean assetApprovedByIssuer, String issuer, String apxRegistry, String info, String name, String symbol, java.math.BigInteger totalAmountRaised, java.math.BigInteger totalTokensSold, java.math.BigInteger highestTokenSellPrice, java.math.BigInteger totalTokensLocked, java.math.BigInteger totalTokensLockedAndLiquidated, Boolean liquidated, java.math.BigInteger liquidationFundsTotal, java.math.BigInteger liquidationTimestamp, java.math.BigInteger liquidationFundsClaimed) {
            super(new org.web3j.abi.datatypes.generated.Uint256(id),new org.web3j.abi.datatypes.Address(contractAddress),new org.web3j.abi.datatypes.Utf8String(ansName),new org.web3j.abi.datatypes.generated.Uint256(ansId),new org.web3j.abi.datatypes.Address(createdBy),new org.web3j.abi.datatypes.Address(owner),new org.web3j.abi.datatypes.generated.Uint256(initialTokenSupply),new org.web3j.abi.datatypes.Bool(whitelistRequiredForRevenueClaim),new org.web3j.abi.datatypes.Bool(whitelistRequiredForLiquidationClaim),new org.web3j.abi.datatypes.Bool(assetApprovedByIssuer),new org.web3j.abi.datatypes.Address(issuer),new org.web3j.abi.datatypes.Address(apxRegistry),new org.web3j.abi.datatypes.Utf8String(info),new org.web3j.abi.datatypes.Utf8String(name),new org.web3j.abi.datatypes.Utf8String(symbol),new org.web3j.abi.datatypes.generated.Uint256(totalAmountRaised),new org.web3j.abi.datatypes.generated.Uint256(totalTokensSold),new org.web3j.abi.datatypes.generated.Uint256(highestTokenSellPrice),new org.web3j.abi.datatypes.generated.Uint256(totalTokensLocked),new org.web3j.abi.datatypes.generated.Uint256(totalTokensLockedAndLiquidated),new org.web3j.abi.datatypes.Bool(liquidated),new org.web3j.abi.datatypes.generated.Uint256(liquidationFundsTotal),new org.web3j.abi.datatypes.generated.Uint256(liquidationTimestamp),new org.web3j.abi.datatypes.generated.Uint256(liquidationFundsClaimed));
            this.id = id;
            this.contractAddress = contractAddress;
            this.ansName = ansName;
            this.ansId = ansId;
            this.createdBy = createdBy;
            this.owner = owner;
            this.initialTokenSupply = initialTokenSupply;
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

        public AssetState(org.web3j.abi.datatypes.generated.Uint256 id, org.web3j.abi.datatypes.Address contractAddress, org.web3j.abi.datatypes.Utf8String ansName, org.web3j.abi.datatypes.generated.Uint256 ansId, org.web3j.abi.datatypes.Address createdBy, org.web3j.abi.datatypes.Address owner, org.web3j.abi.datatypes.generated.Uint256 initialTokenSupply, org.web3j.abi.datatypes.Bool whitelistRequiredForRevenueClaim, org.web3j.abi.datatypes.Bool whitelistRequiredForLiquidationClaim, org.web3j.abi.datatypes.Bool assetApprovedByIssuer, org.web3j.abi.datatypes.Address issuer, org.web3j.abi.datatypes.Address apxRegistry, org.web3j.abi.datatypes.Utf8String info, org.web3j.abi.datatypes.Utf8String name, org.web3j.abi.datatypes.Utf8String symbol, org.web3j.abi.datatypes.generated.Uint256 totalAmountRaised, org.web3j.abi.datatypes.generated.Uint256 totalTokensSold, org.web3j.abi.datatypes.generated.Uint256 highestTokenSellPrice, org.web3j.abi.datatypes.generated.Uint256 totalTokensLocked, org.web3j.abi.datatypes.generated.Uint256 totalTokensLockedAndLiquidated, org.web3j.abi.datatypes.Bool liquidated, org.web3j.abi.datatypes.generated.Uint256 liquidationFundsTotal, org.web3j.abi.datatypes.generated.Uint256 liquidationTimestamp, org.web3j.abi.datatypes.generated.Uint256 liquidationFundsClaimed) {
            super(id,contractAddress,ansName,ansId,createdBy,owner,initialTokenSupply,whitelistRequiredForRevenueClaim,whitelistRequiredForLiquidationClaim,assetApprovedByIssuer,issuer,apxRegistry,info,name,symbol,totalAmountRaised,totalTokensSold,highestTokenSellPrice,totalTokensLocked,totalTokensLockedAndLiquidated,liquidated,liquidationFundsTotal,liquidationTimestamp,liquidationFundsClaimed);
            this.id = id.getValue();
            this.contractAddress = contractAddress.getValue();
            this.ansName = ansName.getValue();
            this.ansId = ansId.getValue();
            this.createdBy = createdBy.getValue();
            this.owner = owner.getValue();
            this.initialTokenSupply = initialTokenSupply.getValue();
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
