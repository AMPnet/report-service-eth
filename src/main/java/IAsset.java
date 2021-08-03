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

        public String owner;

        public String mirroredToken;

        public java.math.BigInteger initialTokenSupply;

        public Boolean whitelistRequiredForTransfer;

        public String issuer;

        public String info;

        public String name;

        public String symbol;

        public AssetState(java.math.BigInteger id, String owner, String mirroredToken, java.math.BigInteger initialTokenSupply, Boolean whitelistRequiredForTransfer, String issuer, String info, String name, String symbol) {
            super(new org.web3j.abi.datatypes.generated.Uint256(id),new org.web3j.abi.datatypes.Address(owner),new org.web3j.abi.datatypes.Address(mirroredToken),new org.web3j.abi.datatypes.generated.Uint256(initialTokenSupply),new org.web3j.abi.datatypes.Bool(whitelistRequiredForTransfer),new org.web3j.abi.datatypes.Address(issuer),new org.web3j.abi.datatypes.Utf8String(info),new org.web3j.abi.datatypes.Utf8String(name),new org.web3j.abi.datatypes.Utf8String(symbol));
            this.id = id;
            this.owner = owner;
            this.mirroredToken = mirroredToken;
            this.initialTokenSupply = initialTokenSupply;
            this.whitelistRequiredForTransfer = whitelistRequiredForTransfer;
            this.issuer = issuer;
            this.info = info;
            this.name = name;
            this.symbol = symbol;
        }

        public AssetState(org.web3j.abi.datatypes.generated.Uint256 id, org.web3j.abi.datatypes.Address owner, org.web3j.abi.datatypes.Address mirroredToken, org.web3j.abi.datatypes.generated.Uint256 initialTokenSupply, org.web3j.abi.datatypes.Bool whitelistRequiredForTransfer, org.web3j.abi.datatypes.Address issuer, org.web3j.abi.datatypes.Utf8String info, org.web3j.abi.datatypes.Utf8String name, org.web3j.abi.datatypes.Utf8String symbol) {
            super(id,owner,mirroredToken,initialTokenSupply,whitelistRequiredForTransfer,issuer,info,name,symbol);
            this.id = id.getValue();
            this.owner = owner.getValue();
            this.mirroredToken = mirroredToken.getValue();
            this.initialTokenSupply = initialTokenSupply.getValue();
            this.whitelistRequiredForTransfer = whitelistRequiredForTransfer.getValue();
            this.issuer = issuer.getValue();
            this.info = info.getValue();
            this.name = name.getValue();
            this.symbol = symbol.getValue();
        }
    }
}
