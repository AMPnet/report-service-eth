package com.ampnet.reportserviceeth.grpc.userservice

import com.ampnet.identityservice.proto.UserResponse

interface UserService {
    fun getUser(address: String): UserResponse
    fun getUsers(addresses: Set<String>): List<UserResponse>
    fun getUsersForIssuer(issuer: String): List<UserResponse>
}
