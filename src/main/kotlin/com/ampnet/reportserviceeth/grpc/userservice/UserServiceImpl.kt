package com.ampnet.reportserviceeth.grpc.userservice

import com.ampnet.identityservice.proto.GetUsersRequest
import com.ampnet.identityservice.proto.IdentityServiceGrpc
import com.ampnet.identityservice.proto.UserResponse
import com.ampnet.reportserviceeth.blockchain.BlockchainService
import com.ampnet.reportserviceeth.config.ApplicationProperties
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.GrpcException
import com.ampnet.reportserviceeth.exception.InvalidRequestException
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import io.grpc.StatusRuntimeException
import mu.KLogging
import net.devh.boot.grpc.client.channelfactory.GrpcChannelFactory
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import kotlin.jvm.Throws

@Service
class UserServiceImpl(
    private val grpcChannelFactory: GrpcChannelFactory,
    private val applicationProperties: ApplicationProperties,
    private val blockchainService: BlockchainService
) : UserService {

    companion object : KLogging()

    private val serviceBlockingStub: IdentityServiceGrpc.IdentityServiceBlockingStub by lazy {
        val channel = grpcChannelFactory.createChannel("identity-service")
        IdentityServiceGrpc.newBlockingStub(channel)
    }

    @Throws(GrpcException::class)
    override fun getUser(address: String): UserResponse = getUsers(setOf(address)).firstOrNull()
        ?: throw InvalidRequestException(ErrorCode.USER_MISSING_INFO, "Missing user for address: $address")

    @Throws(GrpcException::class)
    override fun getUsers(addresses: Set<String>): List<UserResponse> {
        if (addresses.isEmpty()) return emptyList()
        logger.debug { "Fetching users for addresses: $addresses" }
        try {
            val request = GetUsersRequest.newBuilder()
                .addAllAddresses(addresses)
                .build()
            val response = serviceWithTimeout().getUsers(request).usersList
            logger.debug { "Fetched users: ${response.size}" }
            return response
        } catch (ex: StatusRuntimeException) {
            logger.warn(ex.localizedMessage)
            throw GrpcException(ErrorCode.INT_GRPC_USER, "Failed to fetch users")
        }
    }

    @Throws(GrpcException::class)
    override fun getUsersForIssuer(issuerRequest: IssuerRequest): List<UserResponse> {
        val addresses = blockchainService.getWhitelistedAddress(issuerRequest)
        return getUsers(addresses.toSet())
    }

    private fun serviceWithTimeout() = serviceBlockingStub
        .withDeadlineAfter(applicationProperties.grpc.identityServiceTimeout, TimeUnit.MILLISECONDS)
}
