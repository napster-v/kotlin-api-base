package com.g10.api.base.auth

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

class LoginRequest {
    @NotBlank
    @NotEmpty
    lateinit var username: String

    @NotBlank
    @NotEmpty
    lateinit var password: String
}