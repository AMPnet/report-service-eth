// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./IVersioned.sol";
import "./Structs.sol";

interface ICampaignCommon is IVersioned {
    function commonState() external view returns (Structs.CampaignCommonState memory);
}
