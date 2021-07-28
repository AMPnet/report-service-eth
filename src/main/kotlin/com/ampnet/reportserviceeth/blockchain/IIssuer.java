package com.ampnet.reportserviceeth.blockchain;

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
public class IIssuer extends org.web3j.tx.Contract {
    public static final String BINARY = "";

    public static final String FUNC__DEFINE_STRUCT_AUDITOR = "_define_struct_Auditor";

    public static final String FUNC_GETCAMPAIGNRECORDS = "getCampaignRecords";

    public static final String FUNC_GETSTATE = "getState";

    public static final String FUNC_GETWALLETRECORDS = "getWalletRecords";

    public static final String FUNC_ISWALLETAPPROVED = "isWalletApproved";

    public static final String FUNC_SETINFO = "setInfo";

    @Deprecated
    protected IIssuer(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, java.math.BigInteger gasPrice, java.math.BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected IIssuer(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected IIssuer(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, java.math.BigInteger gasPrice, java.math.BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected IIssuer(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public org.web3j.protocol.core.RemoteFunctionCall<org.web3j.protocol.core.methods.response.TransactionReceipt> _define_struct_Auditor(com.ampnet.reportserviceeth.blockchain.IIssuer.WalletRecord auditor) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC__DEFINE_STRUCT_AUDITOR, 
                java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(auditor),
                java.util.Collections.<org.web3j.abi.TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public org.web3j.protocol.core.RemoteFunctionCall<java.util.List> getCampaignRecords() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETCAMPAIGNRECORDS,
                java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(),
                java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.DynamicArray<IIssuer.WalletRecord>>() {}));
        return new org.web3j.protocol.core.RemoteFunctionCall<java.util.List>(function,
                new java.util.concurrent.Callable<java.util.List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public java.util.List call() throws Exception {
                        java.util.List<org.web3j.abi.datatypes.Type> result = (java.util.List<org.web3j.abi.datatypes.Type>) executeCallSingleValueReturn(function, java.util.List.class);
                        return convertToNative(result);
                    }
                });
    }

    public org.web3j.protocol.core.RemoteFunctionCall<IIssuer.IssuerState> getState() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETSTATE,
                java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(),
                java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<IIssuer.IssuerState>() {}));
        return executeRemoteCallSingleValueReturn(function, com.ampnet.reportserviceeth.blockchain.IIssuer.IssuerState.class);
    }

    public org.web3j.protocol.core.RemoteFunctionCall<java.util.List> getWalletRecords() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETWALLETRECORDS,
                java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(),
                java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.DynamicArray<IIssuer.WalletRecord>>() {}));
        return new org.web3j.protocol.core.RemoteFunctionCall<java.util.List>(function,
                new java.util.concurrent.Callable<java.util.List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public java.util.List call() throws Exception {
                        java.util.List<org.web3j.abi.datatypes.Type> result = (java.util.List<org.web3j.abi.datatypes.Type>) executeCallSingleValueReturn(function, java.util.List.class);
                        return convertToNative(result);
                    }
                });
    }

    public org.web3j.protocol.core.RemoteFunctionCall<Boolean> isWalletApproved(String _wallet) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISWALLETAPPROVED,
                java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(new org.web3j.abi.datatypes.Address(160, _wallet)),
                java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public org.web3j.protocol.core.RemoteFunctionCall<org.web3j.protocol.core.methods.response.TransactionReceipt> setInfo(String info) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETINFO, 
                java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(new org.web3j.abi.datatypes.Utf8String(info)),
                java.util.Collections.<org.web3j.abi.TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static IIssuer load(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, java.math.BigInteger gasPrice, java.math.BigInteger gasLimit) {
        return new IIssuer(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static IIssuer load(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, java.math.BigInteger gasPrice, java.math.BigInteger gasLimit) {
        return new IIssuer(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static IIssuer load(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return new IIssuer(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static IIssuer load(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return new IIssuer(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static org.web3j.protocol.core.RemoteCall<IIssuer> deploy(org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IIssuer.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static org.web3j.protocol.core.RemoteCall<IIssuer> deploy(org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, java.math.BigInteger gasPrice, java.math.BigInteger gasLimit) {
        return deployRemoteCall(IIssuer.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static org.web3j.protocol.core.RemoteCall<IIssuer> deploy(org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IIssuer.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static org.web3j.protocol.core.RemoteCall<IIssuer> deploy(org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, java.math.BigInteger gasPrice, java.math.BigInteger gasLimit) {
        return deployRemoteCall(IIssuer.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class WalletRecord extends org.web3j.abi.datatypes.StaticStruct {
        public String wallet;

        public Boolean whitelisted;

        public WalletRecord(String wallet, Boolean whitelisted) {
            super(new org.web3j.abi.datatypes.Address(wallet),new org.web3j.abi.datatypes.Bool(whitelisted));
            this.wallet = wallet;
            this.whitelisted = whitelisted;
        }

        public WalletRecord(org.web3j.abi.datatypes.Address wallet, org.web3j.abi.datatypes.Bool whitelisted) {
            super(wallet,whitelisted);
            this.wallet = wallet.getValue();
            this.whitelisted = whitelisted.getValue();
        }
    }

    public static class IssuerState extends org.web3j.abi.datatypes.DynamicStruct {
        public java.math.BigInteger id;

        public String owner;

        public String stablecoin;

        public String walletApprover;

        public String info;

        public IssuerState(java.math.BigInteger id, String owner, String stablecoin, String walletApprover, String info) {
            super(new org.web3j.abi.datatypes.generated.Uint256(id),new org.web3j.abi.datatypes.Address(owner),new org.web3j.abi.datatypes.Address(stablecoin),new org.web3j.abi.datatypes.Address(walletApprover),new org.web3j.abi.datatypes.Utf8String(info));
            this.id = id;
            this.owner = owner;
            this.stablecoin = stablecoin;
            this.walletApprover = walletApprover;
            this.info = info;
        }

        public IssuerState(org.web3j.abi.datatypes.generated.Uint256 id, org.web3j.abi.datatypes.Address owner, org.web3j.abi.datatypes.Address stablecoin, org.web3j.abi.datatypes.Address walletApprover, org.web3j.abi.datatypes.Utf8String info) {
            super(id,owner,stablecoin,walletApprover,info);
            this.id = id.getValue();
            this.owner = owner.getValue();
            this.stablecoin = stablecoin.getValue();
            this.walletApprover = walletApprover.getValue();
            this.info = info.getValue();
        }
    }
}
