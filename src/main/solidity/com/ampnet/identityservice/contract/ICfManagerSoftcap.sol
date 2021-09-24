// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./shared/Structs.sol";
import "./shared/ICampaignCommon.sol";

interface ICfManagerSoftcap is ICampaignCommon {
    function getInfoHistory() external view returns (Structs.InfoEntry[] memory);
    function getState() external view returns (Structs.CfManagerSoftcapState memory);
}
