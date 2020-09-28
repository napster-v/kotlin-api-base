package com.g10.api.base.utils

import com.g10.api.base.auth.AppUser
import com.g10.api.base.jwt.Token
import com.g10.api.base.jwt.TokenResponse
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.stereotype.Service
import java.sql.Date
import java.time.LocalDate

@Service
class JWT {
    fun generateAuthTokens(user: AppUser): TokenResponse {
        return createToken(user)
    }

    private fun createToken(user: AppUser): TokenResponse {
        val jws: String = Jwts.builder()
            .setClaims(mapOf("user" to user.id))
            .setSubject(user.username)
            .setIssuedAt(Date.valueOf(LocalDate.now()))
            .setExpiration(
                Date.valueOf(
                    LocalDate.now()
                        .plusDays(1)
                )
            )
            .signWith(Keys.hmacShaKeyFor(SECRET.toByteArray()))
            .setHeader(mapOf("typ" to "JWT"))
            .compact()
        return TokenResponse(Token(jws, jws), user)
    }

    @Throws(SignatureException::class)
    fun getClaims(token: String?): Claims {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(SECRET.toByteArray())
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: SignatureException) {
            e.printStackTrace()
            throw SignatureException("Token not valid")
        }
    }

    fun extractUsername(token: String?): String {
        return getClaims(token).subject
    }

    companion object {
        const val SECRET = "*xdoq1o+p($6nnt9p^l=2smj5tpl^v&#m7luh0z)u69odvc=4$/n"
    }
}