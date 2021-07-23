package com.ampnet.reportserviceeth.exception

enum class ErrorCode(val categoryCode: String, var specificCode: String, var message: String) {

    // Users: 03
    USER_MISSING_INFO("03", "07", "No user with info on the platform"),
    USER_NOT_ISSUER("03", "02", "User is not the issuer owner"),

    // Internal: 08
    INT_GRPC_BLOCKCHAIN("08", "03", "Failed gRPC call to blockchain service"),
    INT_GRPC_USER("08", "04", "Failed gRPC call to user service"),
    INT_REQUEST("08", "08", "Invalid controller request exception"),
    INT_GENERATING_PDF("08", "10", "Could not generate pdf from data"),
    INT_UNSUPPORTED_TX("08", "11", "Unsupported transaction"),
    INT_GENERATING_XLSX("08", "14", "Could not generate xlsx file"),

    // Middleware: 11
    MIDDLEWARE("11", "00", "Undefined")
}
