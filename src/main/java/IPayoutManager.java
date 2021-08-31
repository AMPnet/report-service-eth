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
public class IPayoutManager extends org.web3j.tx.Contract {
    public static final String BINARY = "";

    public static final String FUNC_GETSTATE = "getState";

    @Deprecated
    protected IPayoutManager(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, java.math.BigInteger gasPrice, java.math.BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected IPayoutManager(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    protected IPayoutManager(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public org.web3j.protocol.core.RemoteFunctionCall<IPayoutManager.PayoutManagerState> getState() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETSTATE,
                java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(),
                java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<IPayoutManager.PayoutManagerState>() {}));
        return executeRemoteCallSingleValueReturn(function, IPayoutManager.PayoutManagerState.class);
    }

    public static IPayoutManager load(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return new IPayoutManager(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static IPayoutManager load(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return new IPayoutManager(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static org.web3j.protocol.core.RemoteCall<IPayoutManager> deploy(org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IPayoutManager.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static org.web3j.protocol.core.RemoteCall<IPayoutManager> deploy(org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IPayoutManager.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    public static class PayoutManagerState extends org.web3j.abi.datatypes.DynamicStruct {
        public java.math.BigInteger id;

        public String contractAddress;

        public String ansName;

        public java.math.BigInteger ansId;

        public String createdBy;

        public String owner;

        public String asset;

        public String assetFactory;

        public java.math.BigInteger totalPayoutsCreated;

        public java.math.BigInteger totalPayoutsAmount;

        public String info;

        public PayoutManagerState(java.math.BigInteger id, String contractAddress, String ansName, java.math.BigInteger ansId, String createdBy, String owner, String asset, String assetFactory, java.math.BigInteger totalPayoutsCreated, java.math.BigInteger totalPayoutsAmount, String info) {
            super(new org.web3j.abi.datatypes.generated.Uint256(id),new org.web3j.abi.datatypes.Address(contractAddress),new org.web3j.abi.datatypes.Utf8String(ansName),new org.web3j.abi.datatypes.generated.Uint256(ansId),new org.web3j.abi.datatypes.Address(createdBy),new org.web3j.abi.datatypes.Address(owner),new org.web3j.abi.datatypes.Address(asset),new org.web3j.abi.datatypes.Address(assetFactory),new org.web3j.abi.datatypes.generated.Uint256(totalPayoutsCreated),new org.web3j.abi.datatypes.generated.Uint256(totalPayoutsAmount),new org.web3j.abi.datatypes.Utf8String(info));
            this.id = id;
            this.contractAddress = contractAddress;
            this.ansName = ansName;
            this.ansId = ansId;
            this.createdBy = createdBy;
            this.owner = owner;
            this.asset = asset;
            this.assetFactory = assetFactory;
            this.totalPayoutsCreated = totalPayoutsCreated;
            this.totalPayoutsAmount = totalPayoutsAmount;
            this.info = info;
        }

        public PayoutManagerState(org.web3j.abi.datatypes.generated.Uint256 id, org.web3j.abi.datatypes.Address contractAddress, org.web3j.abi.datatypes.Utf8String ansName, org.web3j.abi.datatypes.generated.Uint256 ansId, org.web3j.abi.datatypes.Address createdBy, org.web3j.abi.datatypes.Address owner, org.web3j.abi.datatypes.Address asset, org.web3j.abi.datatypes.Address assetFactory, org.web3j.abi.datatypes.generated.Uint256 totalPayoutsCreated, org.web3j.abi.datatypes.generated.Uint256 totalPayoutsAmount, org.web3j.abi.datatypes.Utf8String info) {
            super(id,contractAddress,ansName,ansId,createdBy,owner,asset,assetFactory,totalPayoutsCreated,totalPayoutsAmount,info);
            this.id = id.getValue();
            this.contractAddress = contractAddress.getValue();
            this.ansName = ansName.getValue();
            this.ansId = ansId.getValue();
            this.createdBy = createdBy.getValue();
            this.owner = owner.getValue();
            this.asset = asset.getValue();
            this.assetFactory = assetFactory.getValue();
            this.totalPayoutsCreated = totalPayoutsCreated.getValue();
            this.totalPayoutsAmount = totalPayoutsAmount.getValue();
            this.info = info.getValue();
        }
    }
}
