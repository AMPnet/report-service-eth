// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./IVersioned.sol";
import "./Structs.sol";

interface ICampaignCommon is IVersioned {
    function commonState() external view returns (Structs.CampaignCommonState memory);
    function investmentAmount(address investor) external view returns (uint256);
    function tokenAmount(address investor) external view returns (uint256);
    function claimedAmount(address investor) external view returns (uint256);
}
