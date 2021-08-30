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
public class IPayoutManagerFactory extends org.web3j.tx.Contract {
    public static final String BINARY = "";

    public static final String FUNC_GETINSTANCES = "getInstances";

    protected IPayoutManagerFactory(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    protected IPayoutManagerFactory(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public org.web3j.protocol.core.RemoteFunctionCall<java.util.List> getInstances() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETINSTANCES,
                java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(),
                java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>>() {}));
        return new org.web3j.protocol.core.RemoteFunctionCall<>(function,
                () -> {
                    java.util.List<org.web3j.abi.datatypes.Type> result = (java.util.List<org.web3j.abi.datatypes.Type>) executeCallSingleValueReturn(function, java.util.List.class);
                    return convertToNative(result);
                });
    }

    public static IPayoutManagerFactory load(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return new IPayoutManagerFactory(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static IPayoutManagerFactory load(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return new IPayoutManagerFactory(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static org.web3j.protocol.core.RemoteCall<IPayoutManagerFactory> deploy(org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IPayoutManagerFactory.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static org.web3j.protocol.core.RemoteCall<IPayoutManagerFactory> deploy(org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IPayoutManagerFactory.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }
}
