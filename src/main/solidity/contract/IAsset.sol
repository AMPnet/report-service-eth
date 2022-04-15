// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./shared/Structs.sol";
import "./shared/IAssetCommon.sol";

interface IAsset is IAssetCommon {
    function getState() external view returns (Structs.AssetState memory);
    function getInfoHistory() external view returns (Structs.InfoEntry[] memory);
    function getSellHistory() external view returns (Structs.TokenSaleInfo[] memory);
}
