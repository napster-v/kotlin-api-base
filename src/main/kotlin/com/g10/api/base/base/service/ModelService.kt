package com.g10.api.base.base.service

import com.g10.api.base.auth.AppUser
import com.g10.api.base.base.dto.BaseDTO
import com.g10.api.base.base.dto.BaseUserDTO
import com.g10.api.base.base.model.AppBaseModel
import com.g10.api.base.base.model.AppBaseUserModel
import com.g10.api.base.renderers.PaginatedResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import javax.servlet.http.HttpServletRequest

interface ModelService<S : AppBaseModel, U : BaseDTO, V : Specification<S>> {
    fun save(dto: U): S
    fun deleteById(id: Long)
    fun findAll(): List<U>
    fun findAll(sort: Sort): List<U>
    fun findAll(specification: V, pageable: Pageable, request: HttpServletRequest): PaginatedResponse<S>
    fun findAll(specification: V): List<U>
    fun findById(id: Long): U
    fun updateById(id: Long, dto: U): S
}

interface ModelAuthService<S : AppBaseUserModel, U : BaseUserDTO, V : Specification<S>> {
    fun findAll(user: AppUser): List<U>
    fun findAll(specification: V, sort: Sort): List<U>
    fun findAll(specification: V, pageable: Pageable, request: HttpServletRequest): PaginatedResponse<S>
    fun findAll(specification: V): List<U>
    fun findById(id: Long, user: AppUser): U
    fun save(dto: U, user: AppUser): S
    fun updateById(id: Long, dto: U, user: AppUser): S
    fun deleteById(id: Long, user: AppUser)
}