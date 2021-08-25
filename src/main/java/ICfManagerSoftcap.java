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
 * <p>Generated with web3j version 4.8.4.
 */
@SuppressWarnings("rawtypes")
public class ICfManagerSoftcap extends Contract {
    public static final String BINARY = "";

    public static final String FUNC_GETSTATE = "getState";

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

    public RemoteFunctionCall<CfManagerSoftcapState> getState() {
        final Function function = new Function(FUNC_GETSTATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<CfManagerSoftcapState>() {}));
        return executeRemoteCallSingleValueReturn(function, CfManagerSoftcapState.class);
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

    public static class CfManagerSoftcapState extends DynamicStruct {
        public BigInteger id;

        public String contractAddress;

        public String ansName;

        public BigInteger ansId;

        public String createdBy;

        public String owner;

        public String asset;

        public String issuer;

        public BigInteger tokenPrice;

        public BigInteger softCap;

        public BigInteger minInvestment;

        public BigInteger maxInvestment;

        public Boolean whitelistRequired;

        public Boolean finalized;

        public Boolean cancelled;

        public BigInteger totalClaimableTokens;

        public BigInteger totalInvestorsCount;

        public BigInteger totalClaimsCount;

        public BigInteger totalFundsRaised;

        public BigInteger totalTokensSold;

        public BigInteger totalTokensBalance;

        public String info;

        public CfManagerSoftcapState(BigInteger id, String contractAddress, String ansName, BigInteger ansId, String createdBy, String owner, String asset, String issuer, BigInteger tokenPrice, BigInteger softCap, BigInteger minInvestment, BigInteger maxInvestment, Boolean whitelistRequired, Boolean finalized, Boolean cancelled, BigInteger totalClaimableTokens, BigInteger totalInvestorsCount, BigInteger totalClaimsCount, BigInteger totalFundsRaised, BigInteger totalTokensSold, BigInteger totalTokensBalance, String info) {
            super(new Uint256(id),new Address(contractAddress),new Utf8String(ansName),new Uint256(ansId),new Address(createdBy),new Address(owner),new Address(asset),new Address(issuer),new Uint256(tokenPrice),new Uint256(softCap),new Uint256(minInvestment),new Uint256(maxInvestment),new Bool(whitelistRequired),new Bool(finalized),new Bool(cancelled),new Uint256(totalClaimableTokens),new Uint256(totalInvestorsCount),new Uint256(totalClaimsCount),new Uint256(totalFundsRaised),new Uint256(totalTokensSold),new Uint256(totalTokensBalance),new Utf8String(info));
            this.id = id;
            this.contractAddress = contractAddress;
            this.ansName = ansName;
            this.ansId = ansId;
            this.createdBy = createdBy;
            this.owner = owner;
            this.asset = asset;
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

        public CfManagerSoftcapState(Uint256 id, Address contractAddress, Utf8String ansName, Uint256 ansId, Address createdBy, Address owner, Address asset, Address issuer, Uint256 tokenPrice, Uint256 softCap, Uint256 minInvestment, Uint256 maxInvestment, Bool whitelistRequired, Bool finalized, Bool cancelled, Uint256 totalClaimableTokens, Uint256 totalInvestorsCount, Uint256 totalClaimsCount, Uint256 totalFundsRaised, Uint256 totalTokensSold, Uint256 totalTokensBalance, Utf8String info) {
            super(id,contractAddress,ansName,ansId,createdBy,owner,asset,issuer,tokenPrice,softCap,minInvestment,maxInvestment,whitelistRequired,finalized,cancelled,totalClaimableTokens,totalInvestorsCount,totalClaimsCount,totalFundsRaised,totalTokensSold,totalTokensBalance,info);
            this.id = id.getValue();
            this.contractAddress = contractAddress.getValue();
            this.ansName = ansName.getValue();
            this.ansId = ansId.getValue();
            this.createdBy = createdBy.getValue();
            this.owner = owner.getValue();
            this.asset = asset.getValue();
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
