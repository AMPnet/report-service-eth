// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

interface IPayoutManagerFactory {
    function getInstances() external view returns (address[] memory);
}
