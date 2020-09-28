package com.g10.api.base.config

import com.g10.api.base.auth.AppUser
import com.g10.api.base.auth.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val repository: UserRepository) : UserDetailsService {
    //    @Cacheable(value = "users", key = "#root.method.name")
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): AppUser? {
        return repository.findByUsername(username).orElseThrow { UsernameNotFoundException("No such user") }
    }
}
