// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract Structs {

    struct IssuerCommonState {
        string flavor;
        string version;
        address contractAddress;
        address owner;
        address stablecoin;
        address walletApprover;
        string info;
    }

    struct IssuerCommonStateWithName {
        IssuerCommonState issuer;
        string mappedName;
    }

    struct AssetCommonState {
        string flavor;
        string version;
        address contractAddress;
        address owner;
        string info;
        string name;
        string symbol;
        uint256 totalSupply;
        uint8 decimals;
        address issuer;
    }

    struct AssetCommonStateWithName {
        AssetCommonState asset;
        string mappedName;
    }

    struct CampaignCommonState {
        string flavor;
        string version;
        address contractAddress;
        address owner;
        string info;
        address asset;
        address stablecoin;
        uint256 softCap;
        bool finalized;
        bool canceled;
        uint256 pricePerToken;
        uint256 fundsRaised;
        uint256 tokensSold;
    }

    struct CampaignCommonStateWithName {
        CampaignCommonState campaign;
        string mappedName;
    }

    struct CampaignCommonStateWithNameAndInvestment {
        CampaignCommonState campaign;
        string mappedName;
        uint256 tokenAmount;
        uint256 tokenValue;
    }

    struct SnapshotDistributorCommonStateWithName {
        SnapshotDistributorCommonState distributor;
        string mappedName;
    }

    struct SnapshotDistributorCommonState {
        string flavor;
        string version;
        address contractAddress;
        address owner;
        string info;
        address asset;
        uint256 totalPayoutsCreated;
        uint256 totalPayoutsAmount;
    }

    struct TokenSaleInfo {
        address cfManager;
        uint256 tokenAmount;
        uint256 tokenValue;
        uint256 timestamp;
    }

    struct AssetRecord {
        address originalToken;
        address mirroredToken;
        bool exists;
        bool state;
        uint256 stateUpdatedAt;
        uint256 price;
        uint256 priceUpdatedAt;
        uint256 priceValidUntil;
        uint256 capturedSupply;
        address priceProvider;
    }

    struct TokenPriceRecord {
        uint256 price;
        uint256 updatedAtTimestamp;
        uint256 validUntilTimestamp;
        uint256 capturedSupply;
        address provider;
    }

    struct AssetState {
        string flavor;
        string version;
        address contractAddress;
        address owner;
        uint256 initialTokenSupply;
        bool transferable;
        bool whitelistRequiredForRevenueClaim;
        bool whitelistRequiredForLiquidationClaim;
        bool assetApprovedByIssuer;
        address issuer;
        address apxRegistry;
        string info;
        string name;
        string symbol;
        uint256 totalAmountRaised;
        uint256 totalTokensSold;
        uint256 highestTokenSellPrice;
        uint256 totalTokensLocked;
        uint256 totalTokensLockedAndLiquidated;
        bool liquidated;
        uint256 liquidationFundsTotal;
        uint256 liquidationTimestamp;
        uint256 liquidationFundsClaimed;
    }

    struct AssetTransferableState {
        string flavor;
        string version;
        address contractAddress;
        address owner;
        uint256 initialTokenSupply;
        bool whitelistRequiredForRevenueClaim;
        bool whitelistRequiredForLiquidationClaim;
        bool assetApprovedByIssuer;
        address issuer;
        address apxRegistry;
        string info;
        string name;
        string symbol;
        uint256 totalAmountRaised;
        uint256 totalTokensSold;
        uint256 highestTokenSellPrice;
        bool liquidated;
        uint256 liquidationFundsTotal;
        uint256 liquidationTimestamp;
        uint256 liquidationFundsClaimed;
    }

    struct IssuerState {
        string flavor;
        string version;
        address contractAddress;
        address owner;
        address stablecoin;
        address walletApprover;
        string info;
    }

    struct CfManagerState {
        string flavor;
        string version;
        address contractAddress;
        address owner;
        address asset;
        address issuer;
        address stablecoin;
        uint256 tokenPrice;
        uint256 softCap;
        uint256 minInvestment;
        uint256 maxInvestment;
        bool whitelistRequired;
        bool finalized;
        bool canceled;
        uint256 totalClaimableTokens;
        uint256 totalInvestorsCount;
        uint256 totalFundsRaised;
        uint256 totalTokensSold;
        uint256 totalTokensBalance;
        string info;
        address feeManager;
    }

    struct CfManagerSoftcapState {
        string flavor;
        string version;
        address contractAddress;
        address owner;
        address asset;
        address issuer;
        address stablecoin;
        uint256 tokenPrice;
        uint256 softCap;
        uint256 minInvestment;
        uint256 maxInvestment;
        bool whitelistRequired;
        bool finalized;
        bool canceled;
        uint256 totalClaimableTokens;
        uint256 totalInvestorsCount;
        uint256 totalClaimsCount;
        uint256 totalFundsRaised;
        uint256 totalTokensSold;
        uint256 totalTokensBalance;
        string info;
        address feeManager;
    }

    struct Payout {
        uint256 payoutId; // ID of this payout
        address payoutOwner; // address which created this payout
        string payoutInfo; // payout info (or IPFS hash for info)
        bool isCanceled; // determines if this payout is canceled

        address asset; // asset for which payout is being made
        uint256 totalAssetAmount; // sum of all asset holdings in the snapshot, minus ignored asset address holdings
        address[] ignoredHolderAddresses; // addresses which aren't included in the payout

        bytes32 assetSnapshotMerkleRoot; // Merkle root hash of asset holdings in the snapshot, without ignored addresses
        uint256 assetSnapshotMerkleDepth; // depth of snapshot Merkle tree
        uint256 assetSnapshotBlockNumber; // snapshot block number
        string assetSnapshotMerkleIpfsHash; // IPFS hash of stored asset snapshot Merkle tree

        address rewardAsset; // asset issued as payout reward
        uint256 totalRewardAmount; // total amount of reward asset in this payout
        uint256 remainingRewardAmount; // remaining reward asset amount in this payout
    }

    struct InfoEntry {
        string info;
        uint256 timestamp;
    }

    struct WalletRecord {
        address wallet;
        bool whitelisted;
    }

}
