package com.g10.api.base.error.exceptions

import org.springframework.security.core.AuthenticationException

class TokenNotFoundException(message: String) : AuthenticationException(message)