package com.g10.api.base.auth

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AvatarRepository : CrudRepository<Avatar?, Int>

@Repository
interface UserRepository : CrudRepository<AppUser, Int> {
    fun findByUsername(username: String): Optional<AppUser>
    fun getByUsername(username: String): AppUser
}