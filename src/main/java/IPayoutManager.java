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

    @Deprecated
    protected IPayoutManager(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, java.math.BigInteger gasPrice, java.math.BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected IPayoutManager(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public org.web3j.protocol.core.RemoteFunctionCall<IPayoutManager.PayoutManagerState> getState() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETSTATE,
                java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(),
                java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<IPayoutManager.PayoutManagerState>() {
                }));
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

        public String owner;

        public String asset;

        public String info;

        public PayoutManagerState(java.math.BigInteger id, String owner, String asset, String info) {
            super(new org.web3j.abi.datatypes.generated.Uint256(id),new org.web3j.abi.datatypes.Address(owner),new org.web3j.abi.datatypes.Address(asset),new org.web3j.abi.datatypes.Utf8String(info));
            this.id = id;
            this.owner = owner;
            this.asset = asset;
            this.info = info;
        }

        public PayoutManagerState(org.web3j.abi.datatypes.generated.Uint256 id, org.web3j.abi.datatypes.Address owner, org.web3j.abi.datatypes.Address asset, org.web3j.abi.datatypes.Utf8String info) {
            super(id,owner,asset,info);
            this.id = id.getValue();
            this.owner = owner.getValue();
            this.asset = asset.getValue();
            this.info = info.getValue();
        }
    }
}
