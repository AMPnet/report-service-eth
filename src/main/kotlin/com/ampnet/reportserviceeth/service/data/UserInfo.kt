package com.ampnet.reportserviceeth.service.data

import com.ampnet.identityservice.proto.UserResponse
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

data class UserInfo(
    val address: String,
    val firstName: String,
    val lastName: String,
    val createdAt: LocalDateTime,
    val language: String,
    val dateOfBirth: String,
    val documentNumber: String?,
    val personalNumber: String?,
    val dateOfIssue: String?,
    val dateOfExpiry: String?
) {
    constructor(user: UserResponse) : this(
        user.address,
        user.firstName,
        user.lastName,
        user.createdAt.millisecondsToLocalDateTime(),
        user.language,
        user.dateOfBirth,
        user.documentNumber,
        user.personalNumber,
        user.documentValidFrom,
        user.documentValidUntil
    )
}

fun Long.millisecondsToLocalDateTime(): LocalDateTime =
    Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDateTime()
