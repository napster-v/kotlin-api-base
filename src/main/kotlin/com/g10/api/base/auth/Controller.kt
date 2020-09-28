package com.g10.api.base.auth

import com.g10.api.base.jwt.TokenResponse
import com.g10.api.base.utils.JWT
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("user")
class UserController(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val authenticationManager: AuthenticationManager
) {
    @PostMapping("register")
    fun createUser(@RequestBody @Valid user: AppUser): AppUser {
        user.password = passwordEncoder.encode(user.password)
        return userRepository.save(user)
    }

    @PostMapping("login")
    fun loginUser(@RequestBody @Valid loginRequest: LoginRequest): ResponseEntity<TokenResponse> {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginRequest.username,
                    loginRequest.password
                )
            )
        } catch (e: BadCredentialsException) {
            throw BadCredentialsException("Username or Password is incorrect")
        }
        val user = userRepository.findByUsername(loginRequest.username)
        return if (user.isPresent) {
            val response = JWT().generateAuthTokens(user.get())
            ResponseEntity.ok(response)
        } else {
            throw BadCredentialsException("Failed!")
        }
    }
}
