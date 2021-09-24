// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./IVersioned.sol";
import "./Structs.sol";

interface IAssetCommon is IVersioned {
    function commonState() external view returns (Structs.AssetCommonState memory);
    function priceDecimalsPrecision() external view returns (uint256);
}
