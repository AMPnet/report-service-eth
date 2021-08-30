// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./shared/Structs.sol";

interface IIssuer {

    // This dummy function is needed for Web3j to correctly generate structure class. It is because Web3j does not
    // generate structure classes when structure is used only in an array (e.g. for Structs.WalletRecord[]). We don't need
    // to do this for Structs.IssuerState because we already have a function which uses that structure without an array:
    // function getState() external view returns (Structs.IssuerState memory);
    function _define_struct_Auditor(Structs.WalletRecord memory auditor) external;

    function getState() external view returns (Structs.IssuerState memory);
    function isWalletApproved(address _wallet) external view returns (bool);
    function getWalletRecords() external view returns (Structs.WalletRecord[] memory);
}
