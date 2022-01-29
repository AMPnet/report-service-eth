package com.ampnet.reportserviceeth.grpc.userservice

import com.ampnet.identityservice.proto.UserResponse
import com.ampnet.reportserviceeth.service.data.IssuerCampaignRequest
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import com.ampnet.reportserviceeth.util.WalletAddress

interface UserService {
    fun getUser(address: WalletAddress): UserResponse
    fun getUsers(addresses: Set<WalletAddress>): List<UserResponse>
    fun getUsersForIssuer(issuerRequest: IssuerRequest): List<UserResponse>
    fun getUsersForIssuerAndCampaign(issuerCampaignRequest: IssuerCampaignRequest): List<UserResponse>
}
