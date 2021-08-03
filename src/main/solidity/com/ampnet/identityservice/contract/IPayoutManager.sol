// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./shared/Structs.sol";

interface IPayoutManager {
    function getState() external view returns (Structs.PayoutManagerState memory);
}
