package com.ampnet.reportserviceth.contract;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
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
 * <p>Generated with web3j version 1.4.1.
 */
@SuppressWarnings("rawtypes")
public class TransactionEvents extends Contract {
    public static final String BINARY = "";

    public static final Event CANCELINVESTMENT_EVENT = new Event("CancelInvestment", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event CLAIM_EVENT = new Event("Claim", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event CREATEPAYOUT_EVENT = new Event("CreatePayout", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event FINALIZE_EVENT = new Event("Finalize", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event INVEST_EVENT = new Event("Invest", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event PAYOUTCANCELED_EVENT = new Event("PayoutCanceled", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>(true) {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event PAYOUTCLAIMED_EVENT = new Event("PayoutClaimed", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event PAYOUTCREATED_EVENT = new Event("PayoutCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>(true) {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event RELEASE_EVENT = new Event("Release", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected TransactionEvents(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected TransactionEvents(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected TransactionEvents(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected TransactionEvents(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<CancelInvestmentEventResponse> getCancelInvestmentEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CANCELINVESTMENT_EVENT, transactionReceipt);
        ArrayList<CancelInvestmentEventResponse> responses = new ArrayList<CancelInvestmentEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CancelInvestmentEventResponse typedResponse = new CancelInvestmentEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.investor = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.tokenAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.tokenValue = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<CancelInvestmentEventResponse> cancelInvestmentEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, CancelInvestmentEventResponse>() {
            @Override
            public CancelInvestmentEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CANCELINVESTMENT_EVENT, log);
                CancelInvestmentEventResponse typedResponse = new CancelInvestmentEventResponse();
                typedResponse.log = log;
                typedResponse.investor = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.tokenAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.tokenValue = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<CancelInvestmentEventResponse> cancelInvestmentEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CANCELINVESTMENT_EVENT));
        return cancelInvestmentEventFlowable(filter);
    }

    public List<ClaimEventResponse> getClaimEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CLAIM_EVENT, transactionReceipt);
        ArrayList<ClaimEventResponse> responses = new ArrayList<ClaimEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ClaimEventResponse typedResponse = new ClaimEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.investor = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.tokenAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.tokenValue = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ClaimEventResponse> claimEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ClaimEventResponse>() {
            @Override
            public ClaimEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CLAIM_EVENT, log);
                ClaimEventResponse typedResponse = new ClaimEventResponse();
                typedResponse.log = log;
                typedResponse.investor = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.tokenAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.tokenValue = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ClaimEventResponse> claimEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CLAIM_EVENT));
        return claimEventFlowable(filter);
    }

    public List<CreatePayoutEventResponse> getCreatePayoutEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CREATEPAYOUT_EVENT, transactionReceipt);
        ArrayList<CreatePayoutEventResponse> responses = new ArrayList<CreatePayoutEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CreatePayoutEventResponse typedResponse = new CreatePayoutEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.creator = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.payoutId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<CreatePayoutEventResponse> createPayoutEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, CreatePayoutEventResponse>() {
            @Override
            public CreatePayoutEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CREATEPAYOUT_EVENT, log);
                CreatePayoutEventResponse typedResponse = new CreatePayoutEventResponse();
                typedResponse.log = log;
                typedResponse.creator = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.payoutId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<CreatePayoutEventResponse> createPayoutEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CREATEPAYOUT_EVENT));
        return createPayoutEventFlowable(filter);
    }

    public List<FinalizeEventResponse> getFinalizeEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(FINALIZE_EVENT, transactionReceipt);
        ArrayList<FinalizeEventResponse> responses = new ArrayList<FinalizeEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            FinalizeEventResponse typedResponse = new FinalizeEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.fundsRaised = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.tokensSold = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.tokensRefund = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<FinalizeEventResponse> finalizeEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, FinalizeEventResponse>() {
            @Override
            public FinalizeEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(FINALIZE_EVENT, log);
                FinalizeEventResponse typedResponse = new FinalizeEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.fundsRaised = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.tokensSold = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.tokensRefund = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<FinalizeEventResponse> finalizeEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(FINALIZE_EVENT));
        return finalizeEventFlowable(filter);
    }

    public List<InvestEventResponse> getInvestEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(INVEST_EVENT, transactionReceipt);
        ArrayList<InvestEventResponse> responses = new ArrayList<InvestEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            InvestEventResponse typedResponse = new InvestEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.investor = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.tokenAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.tokenValue = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<InvestEventResponse> investEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, InvestEventResponse>() {
            @Override
            public InvestEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(INVEST_EVENT, log);
                InvestEventResponse typedResponse = new InvestEventResponse();
                typedResponse.log = log;
                typedResponse.investor = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.tokenAmount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.tokenValue = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<InvestEventResponse> investEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(INVEST_EVENT));
        return investEventFlowable(filter);
    }

    public List<PayoutCanceledEventResponse> getPayoutCanceledEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(PAYOUTCANCELED_EVENT, transactionReceipt);
        ArrayList<PayoutCanceledEventResponse> responses = new ArrayList<PayoutCanceledEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PayoutCanceledEventResponse typedResponse = new PayoutCanceledEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.payoutOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.payoutId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.rewardAsset = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.remainingRewardAmount = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<PayoutCanceledEventResponse> payoutCanceledEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, PayoutCanceledEventResponse>() {
            @Override
            public PayoutCanceledEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(PAYOUTCANCELED_EVENT, log);
                PayoutCanceledEventResponse typedResponse = new PayoutCanceledEventResponse();
                typedResponse.log = log;
                typedResponse.payoutOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.payoutId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.asset = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.rewardAsset = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.remainingRewardAmount = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<PayoutCanceledEventResponse> payoutCanceledEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PAYOUTCANCELED_EVENT));
        return payoutCanceledEventFlowable(filter);
    }

    public List<PayoutClaimedEventResponse> getPayoutClaimedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(PAYOUTCLAIMED_EVENT, transactionReceipt);
        ArrayList<PayoutClaimedEventResponse> responses = new ArrayList<PayoutClaimedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PayoutClaimedEventResponse typedResponse = new PayoutClaimedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.payoutId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.balance = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.rewardAsset = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.payoutAmount = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<PayoutClaimedEventResponse> payoutClaimedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, PayoutClaimedEventResponse>() {
            @Override
            public PayoutClaimedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(PAYOUTCLAIMED_EVENT, log);
                PayoutClaimedEventResponse typedResponse = new PayoutClaimedEventResponse();
                typedResponse.log = log;
                typedResponse.wallet = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.payoutId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.asset = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.balance = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.rewardAsset = (String) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.payoutAmount = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<PayoutClaimedEventResponse> payoutClaimedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PAYOUTCLAIMED_EVENT));
        return payoutClaimedEventFlowable(filter);
    }

    public List<PayoutCreatedEventResponse> getPayoutCreatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(PAYOUTCREATED_EVENT, transactionReceipt);
        ArrayList<PayoutCreatedEventResponse> responses = new ArrayList<PayoutCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PayoutCreatedEventResponse typedResponse = new PayoutCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.payoutOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.payoutId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.rewardAsset = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.totalRewardAmount = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<PayoutCreatedEventResponse> payoutCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, PayoutCreatedEventResponse>() {
            @Override
            public PayoutCreatedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(PAYOUTCREATED_EVENT, log);
                PayoutCreatedEventResponse typedResponse = new PayoutCreatedEventResponse();
                typedResponse.log = log;
                typedResponse.payoutOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.payoutId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.asset = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.rewardAsset = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.totalRewardAmount = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<PayoutCreatedEventResponse> payoutCreatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PAYOUTCREATED_EVENT));
        return payoutCreatedEventFlowable(filter);
    }

    public List<ReleaseEventResponse> getReleaseEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(RELEASE_EVENT, transactionReceipt);
        ArrayList<ReleaseEventResponse> responses = new ArrayList<ReleaseEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ReleaseEventResponse typedResponse = new ReleaseEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.investor = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.payoutId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ReleaseEventResponse> releaseEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ReleaseEventResponse>() {
            @Override
            public ReleaseEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(RELEASE_EVENT, log);
                ReleaseEventResponse typedResponse = new ReleaseEventResponse();
                typedResponse.log = log;
                typedResponse.investor = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.asset = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.payoutId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ReleaseEventResponse> releaseEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(RELEASE_EVENT));
        return releaseEventFlowable(filter);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    @Deprecated
    public static TransactionEvents load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new TransactionEvents(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static TransactionEvents load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TransactionEvents(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static TransactionEvents load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new TransactionEvents(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static TransactionEvents load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TransactionEvents(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TransactionEvents> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TransactionEvents.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<TransactionEvents> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TransactionEvents.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<TransactionEvents> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TransactionEvents.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<TransactionEvents> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TransactionEvents.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class CancelInvestmentEventResponse extends BaseEventResponse {
        public String investor;

        public String asset;

        public BigInteger tokenAmount;

        public BigInteger tokenValue;

        public BigInteger timestamp;
    }

    public static class ClaimEventResponse extends BaseEventResponse {
        public String investor;

        public String asset;

        public BigInteger tokenAmount;

        public BigInteger tokenValue;

        public BigInteger timestamp;
    }

    public static class CreatePayoutEventResponse extends BaseEventResponse {
        public String creator;

        public String asset;

        public BigInteger payoutId;

        public BigInteger amount;

        public BigInteger timestamp;
    }

    public static class FinalizeEventResponse extends BaseEventResponse {
        public String owner;

        public String asset;

        public BigInteger fundsRaised;

        public BigInteger tokensSold;

        public BigInteger tokensRefund;

        public BigInteger timestamp;
    }

    public static class InvestEventResponse extends BaseEventResponse {
        public String investor;

        public String asset;

        public BigInteger tokenAmount;

        public BigInteger tokenValue;

        public BigInteger timestamp;
    }

    public static class PayoutCanceledEventResponse extends BaseEventResponse {
        public String payoutOwner;

        public BigInteger payoutId;

        public String asset;

        public String rewardAsset;

        public BigInteger remainingRewardAmount;
    }

    public static class PayoutClaimedEventResponse extends BaseEventResponse {
        public String wallet;

        public BigInteger payoutId;

        public String asset;

        public BigInteger balance;

        public String rewardAsset;

        public BigInteger payoutAmount;
    }

    public static class PayoutCreatedEventResponse extends BaseEventResponse {
        public String payoutOwner;

        public BigInteger payoutId;

        public String asset;

        public String rewardAsset;

        public BigInteger totalRewardAmount;
    }

    public static class ReleaseEventResponse extends BaseEventResponse {
        public String investor;

        public String asset;

        public BigInteger payoutId;

        public BigInteger amount;

        public BigInteger timestamp;
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public BigInteger value;
    }
}
