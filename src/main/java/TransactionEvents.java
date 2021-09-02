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
public class TransactionEvents extends org.web3j.tx.Contract {
    public static final String BINARY = "";

    public static final org.web3j.abi.datatypes.Event CANCELINVESTMENT_EVENT = new org.web3j.abi.datatypes.Event("CancelInvestment",
            java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.Address>(true) {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.Address>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}));
    ;

    public static final org.web3j.abi.datatypes.Event CLAIM_EVENT = new org.web3j.abi.datatypes.Event("Claim",
            java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.Address>(true) {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.Address>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}));
    ;

    public static final org.web3j.abi.datatypes.Event CREATEPAYOUT_EVENT = new org.web3j.abi.datatypes.Event("CreatePayout",
            java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.Address>(true) {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}));
    ;

    public static final org.web3j.abi.datatypes.Event INVEST_EVENT = new org.web3j.abi.datatypes.Event("Invest",
            java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.Address>(true) {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.Address>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}));
    ;

    public static final org.web3j.abi.datatypes.Event TRANSFER_EVENT = new org.web3j.abi.datatypes.Event("Transfer",
            java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.Address>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.Address>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}));
    ;

    public static final org.web3j.abi.datatypes.Event RELEASE_EVENT = new org.web3j.abi.datatypes.Event("Release",
            java.util.Arrays.asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.Address>(true) {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.Address>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}));
    ;

    protected TransactionEvents(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    protected TransactionEvents(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public java.util.List<TransactionEvents.CancelInvestmentEventResponse> getCancelInvestmentEvents(org.web3j.protocol.core.methods.response.TransactionReceipt transactionReceipt) {
        java.util.List<org.web3j.tx.Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CANCELINVESTMENT_EVENT, transactionReceipt);
        java.util.ArrayList<TransactionEvents.CancelInvestmentEventResponse> responses = new java.util.ArrayList<TransactionEvents.CancelInvestmentEventResponse>(valueList.size());
        for (org.web3j.tx.Contract.EventValuesWithLog eventValues : valueList) {
            TransactionEvents.CancelInvestmentEventResponse typedResponse = new TransactionEvents.CancelInvestmentEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.investor = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.tokenAmount = (java.math.BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.tokenValue = (java.math.BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (java.math.BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public io.reactivex.Flowable<TransactionEvents.CancelInvestmentEventResponse> cancelInvestmentEventFlowable(org.web3j.protocol.core.methods.request.EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> {
            org.web3j.tx.Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CANCELINVESTMENT_EVENT, log);
            TransactionEvents.CancelInvestmentEventResponse typedResponse = new TransactionEvents.CancelInvestmentEventResponse();
            typedResponse.log = log;
            typedResponse.investor = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.tokenAmount = (java.math.BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.tokenValue = (java.math.BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (java.math.BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            return typedResponse;
        });
    }

    public io.reactivex.Flowable<TransactionEvents.CancelInvestmentEventResponse> cancelInvestmentEventFlowable(org.web3j.protocol.core.DefaultBlockParameter startBlock, org.web3j.protocol.core.DefaultBlockParameter endBlock) {
        org.web3j.protocol.core.methods.request.EthFilter filter = new org.web3j.protocol.core.methods.request.EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(org.web3j.abi.EventEncoder.encode(CANCELINVESTMENT_EVENT));
        return cancelInvestmentEventFlowable(filter);
    }

    public java.util.List<TransactionEvents.ClaimEventResponse> getClaimEvents(org.web3j.protocol.core.methods.response.TransactionReceipt transactionReceipt) {
        java.util.List<org.web3j.tx.Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CLAIM_EVENT, transactionReceipt);
        java.util.ArrayList<TransactionEvents.ClaimEventResponse> responses = new java.util.ArrayList<TransactionEvents.ClaimEventResponse>(valueList.size());
        for (org.web3j.tx.Contract.EventValuesWithLog eventValues : valueList) {
            TransactionEvents.ClaimEventResponse typedResponse = new TransactionEvents.ClaimEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.investor = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.tokenAmount = (java.math.BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.tokenValue = (java.math.BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (java.math.BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public io.reactivex.Flowable<TransactionEvents.ClaimEventResponse> claimEventFlowable(org.web3j.protocol.core.methods.request.EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> {
            org.web3j.tx.Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CLAIM_EVENT, log);
            TransactionEvents.ClaimEventResponse typedResponse = new TransactionEvents.ClaimEventResponse();
            typedResponse.log = log;
            typedResponse.investor = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.tokenAmount = (java.math.BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.tokenValue = (java.math.BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (java.math.BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            return typedResponse;
        });
    }

    public io.reactivex.Flowable<TransactionEvents.ClaimEventResponse> claimEventFlowable(org.web3j.protocol.core.DefaultBlockParameter startBlock, org.web3j.protocol.core.DefaultBlockParameter endBlock) {
        org.web3j.protocol.core.methods.request.EthFilter filter = new org.web3j.protocol.core.methods.request.EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(org.web3j.abi.EventEncoder.encode(CLAIM_EVENT));
        return claimEventFlowable(filter);
    }

    public java.util.List<TransactionEvents.CreatePayoutEventResponse> getCreatePayoutEvents(org.web3j.protocol.core.methods.response.TransactionReceipt transactionReceipt) {
        java.util.List<org.web3j.tx.Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CREATEPAYOUT_EVENT, transactionReceipt);
        java.util.ArrayList<TransactionEvents.CreatePayoutEventResponse> responses = new java.util.ArrayList<>(valueList.size());
        for (org.web3j.tx.Contract.EventValuesWithLog eventValues : valueList) {
            TransactionEvents.CreatePayoutEventResponse typedResponse = new TransactionEvents.CreatePayoutEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.creator = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.payoutId = (java.math.BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.amount = (java.math.BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (java.math.BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public io.reactivex.Flowable<TransactionEvents.CreatePayoutEventResponse> createPayoutEventFlowable(org.web3j.protocol.core.methods.request.EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> {
            org.web3j.tx.Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CREATEPAYOUT_EVENT, log);
            TransactionEvents.CreatePayoutEventResponse typedResponse = new TransactionEvents.CreatePayoutEventResponse();
            typedResponse.log = log;
            typedResponse.creator = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.payoutId = (java.math.BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.amount = (java.math.BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (java.math.BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            return typedResponse;
        });
    }

    public io.reactivex.Flowable<TransactionEvents.CreatePayoutEventResponse> createPayoutEventFlowable(org.web3j.protocol.core.DefaultBlockParameter startBlock, org.web3j.protocol.core.DefaultBlockParameter endBlock) {
        org.web3j.protocol.core.methods.request.EthFilter filter = new org.web3j.protocol.core.methods.request.EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(org.web3j.abi.EventEncoder.encode(CREATEPAYOUT_EVENT));
        return createPayoutEventFlowable(filter);
    }

    public java.util.List<TransactionEvents.InvestEventResponse> getInvestEvents(org.web3j.protocol.core.methods.response.TransactionReceipt transactionReceipt) {
        java.util.List<org.web3j.tx.Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(INVEST_EVENT, transactionReceipt);
        java.util.ArrayList<TransactionEvents.InvestEventResponse> responses = new java.util.ArrayList<>(valueList.size());
        for (org.web3j.tx.Contract.EventValuesWithLog eventValues : valueList) {
            TransactionEvents.InvestEventResponse typedResponse = new TransactionEvents.InvestEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.investor = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.tokenAmount = (java.math.BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.tokenValue = (java.math.BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (java.math.BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public io.reactivex.Flowable<TransactionEvents.InvestEventResponse> investEventFlowable(org.web3j.protocol.core.methods.request.EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> {
            org.web3j.tx.Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(INVEST_EVENT, log);
            TransactionEvents.InvestEventResponse typedResponse = new TransactionEvents.InvestEventResponse();
            typedResponse.log = log;
            typedResponse.investor = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.tokenAmount = (java.math.BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.tokenValue = (java.math.BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (java.math.BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            return typedResponse;
        });
    }

    public io.reactivex.Flowable<TransactionEvents.InvestEventResponse> investEventFlowable(org.web3j.protocol.core.DefaultBlockParameter startBlock, org.web3j.protocol.core.DefaultBlockParameter endBlock) {
        org.web3j.protocol.core.methods.request.EthFilter filter = new org.web3j.protocol.core.methods.request.EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(org.web3j.abi.EventEncoder.encode(INVEST_EVENT));
        return investEventFlowable(filter);
    }

    public java.util.List<TransactionEvents.TransferEventResponse> getTransferEvents(org.web3j.protocol.core.methods.response.TransactionReceipt transactionReceipt) {
        java.util.List<org.web3j.tx.Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        java.util.ArrayList<TransactionEvents.TransferEventResponse> responses = new java.util.ArrayList<>(valueList.size());
        for (org.web3j.tx.Contract.EventValuesWithLog eventValues : valueList) {
            TransactionEvents.TransferEventResponse typedResponse = new TransactionEvents.TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.value = (java.math.BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public io.reactivex.Flowable<TransactionEvents.TransferEventResponse> transferEventFlowable(org.web3j.protocol.core.methods.request.EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> {
            org.web3j.tx.Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
            TransactionEvents.TransferEventResponse typedResponse = new TransactionEvents.TransferEventResponse();
            typedResponse.log = log;
            typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.value = (java.math.BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            return typedResponse;
        });
    }

    public io.reactivex.Flowable<TransactionEvents.TransferEventResponse> transferEventFlowable(org.web3j.protocol.core.DefaultBlockParameter startBlock, org.web3j.protocol.core.DefaultBlockParameter endBlock) {
        org.web3j.protocol.core.methods.request.EthFilter filter = new org.web3j.protocol.core.methods.request.EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(org.web3j.abi.EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public java.util.List<TransactionEvents.ReleaseEventResponse> getReleaseEvents(org.web3j.protocol.core.methods.response.TransactionReceipt transactionReceipt) {
        java.util.List<org.web3j.tx.Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(RELEASE_EVENT, transactionReceipt);
        java.util.ArrayList<TransactionEvents.ReleaseEventResponse> responses = new java.util.ArrayList<>(valueList.size());
        for (org.web3j.tx.Contract.EventValuesWithLog eventValues : valueList) {
            TransactionEvents.ReleaseEventResponse typedResponse = new TransactionEvents.ReleaseEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.investor = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.payoutId = (java.math.BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.amount = (java.math.BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (java.math.BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public io.reactivex.Flowable<TransactionEvents.ReleaseEventResponse> releaseEventFlowable(org.web3j.protocol.core.methods.request.EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> {
            org.web3j.tx.Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(RELEASE_EVENT, log);
            TransactionEvents.ReleaseEventResponse typedResponse = new TransactionEvents.ReleaseEventResponse();
            typedResponse.log = log;
            typedResponse.investor = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.payoutId = (java.math.BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.amount = (java.math.BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (java.math.BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            return typedResponse;
        });
    }

    public io.reactivex.Flowable<TransactionEvents.ReleaseEventResponse> releaseEventFlowable(org.web3j.protocol.core.DefaultBlockParameter startBlock, org.web3j.protocol.core.DefaultBlockParameter endBlock) {
        org.web3j.protocol.core.methods.request.EthFilter filter = new org.web3j.protocol.core.methods.request.EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(org.web3j.abi.EventEncoder.encode(RELEASE_EVENT));
        return releaseEventFlowable(filter);
    }

    public static TransactionEvents load(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return new TransactionEvents(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static TransactionEvents load(String contractAddress, org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return new TransactionEvents(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static org.web3j.protocol.core.RemoteCall<TransactionEvents> deploy(org.web3j.protocol.Web3j web3j, org.web3j.crypto.Credentials credentials, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TransactionEvents.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static org.web3j.protocol.core.RemoteCall<TransactionEvents> deploy(org.web3j.protocol.Web3j web3j, org.web3j.tx.TransactionManager transactionManager, org.web3j.tx.gas.ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TransactionEvents.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    public static class CancelInvestmentEventResponse extends org.web3j.protocol.core.methods.response.BaseEventResponse {
        public String investor;

        public String asset;

        public java.math.BigInteger tokenAmount;

        public java.math.BigInteger tokenValue;

        public java.math.BigInteger timestamp;
    }

    public static class ClaimEventResponse extends org.web3j.protocol.core.methods.response.BaseEventResponse {
        public String investor;

        public String asset;

        public java.math.BigInteger tokenAmount;

        public java.math.BigInteger tokenValue;

        public java.math.BigInteger timestamp;
    }

    public static class CreatePayoutEventResponse extends org.web3j.protocol.core.methods.response.BaseEventResponse {
        public String creator;

        public String asset;

        public java.math.BigInteger payoutId;

        public java.math.BigInteger amount;

        public java.math.BigInteger timestamp;
    }

    public static class InvestEventResponse extends org.web3j.protocol.core.methods.response.BaseEventResponse {
        public String investor;

        public String asset;

        public java.math.BigInteger tokenAmount;

        public java.math.BigInteger tokenValue;

        public java.math.BigInteger timestamp;
    }

    public static class TransferEventResponse extends org.web3j.protocol.core.methods.response.BaseEventResponse {
        public String from;

        public String to;

        public java.math.BigInteger value;
    }

    public static class ReleaseEventResponse extends org.web3j.protocol.core.methods.response.BaseEventResponse {
        public String investor;

        public String asset;

        public java.math.BigInteger payoutId;

        public java.math.BigInteger amount;

        public java.math.BigInteger timestamp;
    }
}
