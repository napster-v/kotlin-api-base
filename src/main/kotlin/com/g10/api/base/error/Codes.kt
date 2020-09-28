package com.g10.api.base.error

enum class Codes(val code: Int, val error: String, val description: String) {
    USER_NOT_FOUND(1, "User not found", "Requested user not found."), CODE_NOT_FOUND(
        11,
        "Code not found",
        "The code requested does not exists or has already used."
    ),
    ACCOUNT_SUSPENDED(
        2,
        "User suspended",
        "The user account has been suspended and information cannot be retrieved"
    ),
    USER_LOCKED_OUT(
        3,
        "User locked out",
        "The user account has been locked out and information cannot be retrieved"
    ),
    USER_LOCKING_OUT_WARNING(
        4,
        "User locking out warning",
        "The user account is at risk of locking out"
    ),
    ACCOUNT_SUSPENSION_WARNING(
        5,
        "User suspension warning",
        "The user account is at risk of suspension"
    ),
    EXPIRED_OR_INVALID_TOKEN(
        6, "Invalid or expired token",
        "The access token used in the request is incorrect or has expired "
    ),
    AUTH_TOKEN_EXPIRED(9, "Auth token expired", "Authorization failed."), AUTH_TOKEN_NOT_FOUND(
        7,
        "Auth token not found",
        "Please provide access token in the header. Ex. Bearer {token}."
    ),
    AUTH_FAILED(22, "Authentication Failed", "Failed to authenticate this query."), INVALID_AUTH_TOKEN(
        10,
        "Invalid auth token",
        "Token provided is not valid."
    ),
    INVALID_REFRESH_TOKEN(23, "Invalid refresh token", "Refresh Token provided is not valid."), EMAIL_ALREADY_EXISTS(
        15,
        "Email already exists",
        "Email provided already exists with different account."
    ),
    INVALID_METHOD(18, "Invalid method", "Method requested in invalid. Check the API docs."), INTERNAL_ERROR(
        20,
        "Internal error",
        "Something went wrong on our side."
    ),
    FIELDS_ERROR(21, "Fields error", "Fields error."), NOT_PERMITTED(
        24,
        "Not permitted",
        "User not permitted to perform this action."
    ),
    NOT_FOUND(
        25,
        "Not Found",
        "Object not found. The resource you are looking for does not exist or is deleted."
    ),
    INTEGRITY_ERROR(26, "DB Integrity Issue", ""), VALIDATION_ERROR(27, "Validation Error", "Not Permitted.");

}