package com.g10.api.base.base.repositories

import com.g10.api.base.auth.AppUser
import com.g10.api.base.base.model.AppBaseModel
import com.g10.api.base.base.model.AppBaseUserModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.transaction.annotation.Transactional
import java.util.*

@NoRepositoryBean
interface BaseRepository<T : AppBaseModel> : JpaRepository<T, Long>, JpaSpecificationExecutor<T>

@NoRepositoryBean
interface BaseUserRepository<T : AppBaseUserModel> : JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    fun findAllByUser(user: AppUser): List<T>
    fun findByUserAndId(user: AppUser, id: Long): Optional<T>

    @Transactional
    fun deleteByUserAndId(user: AppUser, id: Long)
}