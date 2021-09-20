package com.ampnet.reportserviceth.contract;

import java.math.BigInteger;
import java.util.Arrays;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
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
public class IPayoutManager extends Contract {
    public static final String BINARY = "";

    public static final String FUNC_GETSTATE = "getState";

    @Deprecated
    protected IPayoutManager(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected IPayoutManager(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected IPayoutManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected IPayoutManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<PayoutManagerState> getState() {
        final Function function = new Function(FUNC_GETSTATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<PayoutManagerState>() {}));
        return executeRemoteCallSingleValueReturn(function, PayoutManagerState.class);
    }

    @Deprecated
    public static IPayoutManager load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new IPayoutManager(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static IPayoutManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new IPayoutManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static IPayoutManager load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new IPayoutManager(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static IPayoutManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new IPayoutManager(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<IPayoutManager> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IPayoutManager.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<IPayoutManager> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(IPayoutManager.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<IPayoutManager> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IPayoutManager.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<IPayoutManager> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(IPayoutManager.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class PayoutManagerState extends DynamicStruct {
        public String flavor;

        public String version;

        public String contractAddress;

        public String owner;

        public String asset;

        public BigInteger totalPayoutsCreated;

        public BigInteger totalPayoutsAmount;

        public String info;

        public PayoutManagerState(String flavor, String version, String contractAddress, String owner, String asset, BigInteger totalPayoutsCreated, BigInteger totalPayoutsAmount, String info) {
            super(new Utf8String(flavor),new Utf8String(version),new Address(contractAddress),new Address(owner),new Address(asset),new Uint256(totalPayoutsCreated),new Uint256(totalPayoutsAmount),new Utf8String(info));
            this.flavor = flavor;
            this.version = version;
            this.contractAddress = contractAddress;
            this.owner = owner;
            this.asset = asset;
            this.totalPayoutsCreated = totalPayoutsCreated;
            this.totalPayoutsAmount = totalPayoutsAmount;
            this.info = info;
        }

        public PayoutManagerState(Utf8String flavor, Utf8String version, Address contractAddress, Address owner, Address asset, Uint256 totalPayoutsCreated, Uint256 totalPayoutsAmount, Utf8String info) {
            super(flavor,version,contractAddress,owner,asset,totalPayoutsCreated,totalPayoutsAmount,info);
            this.flavor = flavor.getValue();
            this.version = version.getValue();
            this.contractAddress = contractAddress.getValue();
            this.owner = owner.getValue();
            this.asset = asset.getValue();
            this.totalPayoutsCreated = totalPayoutsCreated.getValue();
            this.totalPayoutsAmount = totalPayoutsAmount.getValue();
            this.info = info.getValue();
        }
    }
}
