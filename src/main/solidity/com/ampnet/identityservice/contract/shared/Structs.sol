// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

interface Structs {

    struct IssuerState {
        uint256 id;
        address owner;
        address stablecoin;
        address walletApprover;
        string info;
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
