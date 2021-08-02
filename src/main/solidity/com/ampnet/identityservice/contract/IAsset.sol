// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./shared/Structs.sol";

interface IAsset {
    function getState() external view returns (Structs.AssetState memory);
}
