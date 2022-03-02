// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./shared/Structs.sol";

interface TransactionEvents {
    event Invest(
        address indexed investor,
        address asset,
        uint256 tokenAmount,
        uint256 tokenValue,
        uint256 timestamp
    );
    event Claim(
        address indexed investor,
        address asset,
        uint256 tokenAmount,
        uint256 tokenValue,
        uint256 timestamp
    );
    event Finalize(
        address indexed owner,
        address asset,
        uint256 fundsRaised,
        uint256 tokensSold,
        uint256 tokensRefund,
        uint256 timestamp
    );
    event CancelInvestment(
        address indexed investor,
        address asset,
        uint256 tokenAmount,
        uint256 tokenValue,
        uint256 timestamp
    );
    event Transfer(
        address from,
        address to,
        uint256 value
    );
    event CreatePayout(
        address indexed creator,
        address asset,
        uint256 payoutId,
        uint256 amount,
        uint256 timestamp
    );
    event Release(
        address indexed investor,
        address asset,
        uint256 payoutId,
        uint256 amount,
        uint256 timestamp
    );
    event PayoutCreated(
        uint256 payoutId,
        address indexed payoutOwner,
        address asset,
        address rewardAsset,
        uint256 totalRewardAmount
    );
    event PayoutCanceled(
        uint256 payoutId,
        address indexed payoutOwner,
        address asset,
        address rewardAsset,
        uint256 remainingRewardAmount
    );
    event PayoutClaimed(
        uint256 payoutId,
        address indexed wallet,
        address asset,
        uint256 balance,
        address rewardAsset,
        uint256 payoutAmount
    );
}
