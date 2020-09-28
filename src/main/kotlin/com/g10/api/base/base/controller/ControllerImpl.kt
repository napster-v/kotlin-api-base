package com.g10.api.base.base.controller

import com.g10.api.base.auth.AppUser
import com.g10.api.base.auth.UserRepository
import com.g10.api.base.base.dto.BaseDTO
import com.g10.api.base.base.dto.BaseUserDTO
import com.g10.api.base.base.model.AppBaseModel
import com.g10.api.base.base.model.AppBaseUserModel
import com.g10.api.base.base.service.ModelAuthService
import com.g10.api.base.base.service.ModelService
import com.g10.api.base.base.specifications.UserSpecification
import com.g10.api.base.renderers.PaginatedResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

abstract class BaseControllerImpl<S : AppBaseModel, U : BaseDTO, E : ModelService<S, U, V>, V : Specification<S>>(
    private val service: E
) :
    BaseController<S, U, V> {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    override fun create(@RequestBody @Valid dto: U): S {
        return service.save(dto)
    }

    @ResponseStatus(HttpStatus.OK)
    override fun list(): List<U> {
        return service.findAll()
    }

    @ResponseStatus(HttpStatus.OK)
    override fun list(sortBy: String): List<U> {
        val sort: Sort = if (sortBy.contains("-")) {
            Sort.by(sortBy.replace("-", ""))
                .descending()
        } else {
            Sort.by(sortBy)
                .ascending()
        }
        return service.findAll(sort)
    }

    @ResponseStatus(HttpStatus.OK)
    override fun list(specification: V): List<U> {
        return service.findAll(specification)
    }

    @ResponseStatus(HttpStatus.OK)
    override fun list(
        page: Int,
        size: Int,
        sortBy: String,
        specification: V,
        request: HttpServletRequest
    ): PaginatedResponse<S> {
        val pageRequest: Pageable = if (sortBy.contains("-")) {
            PageRequest.of(
                page - 1,
                size,
                Sort.by(sortBy.replace("-", ""))
                    .descending()
            )
        } else {
            PageRequest.of(
                page - 1,
                size,
                Sort.by(sortBy)
                    .ascending()
            )
        }
        return service.findAll(specification, pageRequest, request)
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{id}")
    override fun update(id: Long, @RequestBody @Valid dto: U): S {
        return service.updateById(id, dto)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    override fun retrieve(@PathVariable id: Long): U {
        return service.findById(id)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    override fun destroy(@PathVariable id: Long) {
        service.deleteById(id)
    }
}

abstract class BaseAuthControllerImpl<S : AppBaseUserModel, U : BaseUserDTO, E : ModelAuthService<S, U, V>, V : UserSpecification<S>>(
    private val repository: UserRepository, private val service: E
) :
    BaseAuthController<S, U, V> {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    override fun create(@RequestBody @Valid dto: U, principal: Principal): S {
        return service.save(dto, getUserByUsername(principal))
    }

    @ResponseStatus(HttpStatus.OK)
    override fun list(principal: Principal): List<U> {
        return service.findAll(getUserByUsername(principal))
    }

    @ResponseStatus(HttpStatus.OK)
    override fun list(specification: V, sortBy: String): List<U> {
        val sort: Sort = if (sortBy.contains("-")) {
            Sort.by(sortBy.replace("-", ""))
                .descending()
        } else {
            Sort.by(sortBy)
                .ascending()
        }
        return service.findAll(specification, sort)
    }

    @ResponseStatus(HttpStatus.OK)
    override fun list(specification: V): List<U> {
        return service.findAll(specification)
    }

    @ResponseStatus(HttpStatus.OK)
    override fun list(
        page: Int,
        size: Int,
        sortBy: String,
        specification: V,
        request: HttpServletRequest
    ): PaginatedResponse<S> {
        val pageRequest: Pageable = if (sortBy.contains("-")) {
            PageRequest.of(
                page - 1,
                size,
                Sort.by(sortBy.replace("-", ""))
                    .descending()
            )
        } else {
            PageRequest.of(
                page - 1,
                size,
                Sort.by(sortBy)
                    .ascending()
            )
        }
        return service.findAll(specification, pageRequest, request)
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{id}")
    override fun update(id: Long, @RequestBody @Valid dto: U, principal: Principal): S {
        return service.updateById(id, dto, getUserByUsername(principal))
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    override fun retrieve(@PathVariable id: Long, principal: Principal): U {
        return service.findById(id, getUserByUsername(principal))
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    override fun destroy(@PathVariable id: Long, principal: Principal) {
        service.deleteById(id, getUserByUsername(principal))
    }

    override fun getUserByUsername(principal: Principal): AppUser {
        return repository.getByUsername(principal.name)
    }
}