package com.g10.api.base.jwt

import com.g10.api.base.auth.AppUser

class AccessToken {
    val access: String = ""
}

class RefreshToken {
    val refresh: String = ""
}

class Token(val access: String, val refresh: String)

class TokenResponse(val token: Token, val user: AppUser)