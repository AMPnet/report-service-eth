package com.ampnet.reportserviceeth.grpc.userservice

import com.ampnet.identityservice.proto.UserResponse
import com.ampnet.reportserviceeth.service.data.IssuerCampaignRequest
import com.ampnet.reportserviceeth.service.data.IssuerRequest

interface UserService {
    fun getUser(address: String): UserResponse
    fun getUsers(addresses: Set<String>): List<UserResponse>
    fun getUsersForIssuer(issuerRequest: IssuerRequest): List<UserResponse>
    fun getUsersForIssuerAndCampaign(issuerCampaignRequest: IssuerCampaignRequest): List<UserResponse>
}
