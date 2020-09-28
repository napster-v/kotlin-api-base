package com.g10.api.base.error

import org.springframework.http.HttpStatus

class FieldError(var field: String?, var message: String?)

class Error {
    var message: String
    var status: HttpStatus
    var statusCode: Int
    var errorMessage: String
    var errorCode: Int
    var description: String
    var fields: List<FieldError>? = null

    constructor(
        message: String,
        status: HttpStatus, code: Codes
    ) {
        this.message = message
        this.status = status
        statusCode = status.value()
        errorMessage = code.error
        errorCode = code.code
        description = code.description
    }

    constructor(message: String, status: HttpStatus, code: Codes, fields: List<FieldError>?) {
        this.message = message
        this.status = status
        statusCode = status.value()
        errorMessage = code.error
        errorCode = code.code
        description = code.description
        this.fields = fields
    }
}