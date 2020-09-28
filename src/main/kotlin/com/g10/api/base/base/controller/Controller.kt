package com.g10.api.base.base.controller

import com.g10.api.base.auth.AppUser
import com.g10.api.base.base.dto.BaseDTO
import com.g10.api.base.base.model.AppBaseModel
import com.g10.api.base.base.model.AppBaseUserModel
import com.g10.api.base.base.specifications.UserSpecification
import com.g10.api.base.renderers.PaginatedResponse
import org.springframework.data.jpa.domain.Specification
import java.security.Principal
import javax.servlet.http.HttpServletRequest

interface BaseController<S : AppBaseModel, U : BaseDTO, V : Specification<S>> {
    fun list(): List<U>
    fun list(sortBy: String): List<U>
    fun list(specification: V): List<U>
    fun list(
        page: Int,
        size: Int,
        sortBy: String,
        specification: V,
        request: HttpServletRequest
    ): PaginatedResponse<S>

    fun create(dto: U): S
    fun retrieve(id: Long): U
    fun update(id: Long, dto: U): S
    fun destroy(id: Long)
}

interface BaseAuthController<S : AppBaseUserModel, U : BaseDTO, V : UserSpecification<S>> {
    fun list(principal: Principal): List<U>
    fun list(specification: V, sortBy: String): List<U>
    fun list(specification: V): List<U>
    fun list(
        page: Int,
        size: Int,
        sortBy: String,
        specification: V,
        request: HttpServletRequest
    ): PaginatedResponse<S>

    fun retrieve(id: Long, principal: Principal): U
    fun update(id: Long, dto: U, principal: Principal): S
    fun create(dto: U, principal: Principal): S
    fun destroy(id: Long, principal: Principal)
    fun getUserByUsername(principal: Principal): AppUser
}



