package com.g10.api.base.base.service

import com.g10.api.base.auth.AppUser
import com.g10.api.base.base.dto.BaseDTO
import com.g10.api.base.base.dto.BaseUserDTO
import com.g10.api.base.base.mapper.BaseMapper
import com.g10.api.base.base.model.AppBaseModel
import com.g10.api.base.base.model.AppBaseUserModel
import com.g10.api.base.base.repositories.BaseRepository
import com.g10.api.base.base.repositories.BaseUserRepository
import com.g10.api.base.renderers.PaginatedResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import java.util.stream.Collectors
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletRequest

abstract class ModelServiceImpl<S : AppBaseModel, U : BaseDTO, R : BaseRepository<S>, V : Specification<S>> protected constructor(
    private val repository: R
) :
    ModelService<S, U, V>, BaseMapper<S, U> {
    override fun findAll(): List<U> {
        val sList = repository.findAll()
        return sList.stream()
            .map { source: S -> toDto(source) }
            .collect(Collectors.toUnmodifiableList())
    }

    override fun findAll(specification: V): List<U> {
        val sList = repository.findAll(specification)
        return sList.stream()
            .map { source: S -> toDto(source) }
            .collect(Collectors.toUnmodifiableList())
    }

    override fun findAll(sort: Sort): List<U> {
        val sList = repository.findAll(sort)
        return sList.stream()
            .map { source: S -> toDto(source) }
            .collect(Collectors.toUnmodifiableList())
    }

    override fun findAll(specification: V, pageable: Pageable, request: HttpServletRequest): PaginatedResponse<S> {
        val all = repository.findAll(specification, pageable)
        val data = all.content
            .stream()
            .map { source: S -> toDto(source) }
            .collect(Collectors.toUnmodifiableList())
        return PaginatedResponse(all, request, data)
    }

    override fun findById(id: Long): U {
        val s = repository.findById(id)
            .orElseThrow {
                EntityNotFoundException(
                    String.format(
                        "Resource with id %d does not exist or is deleted.",
                        id
                    )
                )
            }
        return toDto(s)
    }

    override fun save(dto: U): S {
        return repository.save(toModel(dto))
    }

    override fun updateById(id: Long, dto: U): S {
        val s = repository.findById(id)
            .orElseThrow {
                EntityNotFoundException(
                    String.format(
                        "Resource with id %d does not exist or is deleted.",
                        id
                    )
                )
            }
        val model: S = toModel(dto)
        model.id = s.id
        return repository.save(s)
    }

    override fun deleteById(id: Long) {
        repository.deleteById(id)
    }
}

abstract class ModelAuthServiceImpl<S : AppBaseUserModel, U : BaseUserDTO, R : BaseUserRepository<S>, V : Specification<S>>(
    private val repository: R
) : ModelAuthService<S, U, V>, BaseMapper<S, U> {

    override fun findAll(user: AppUser): List<U> {
        val sList = repository.findAllByUser(user)
        return sList.stream()
            .map { source: S -> toDto(source) }
            .collect(Collectors.toUnmodifiableList())
    }

    override fun findById(id: Long, user: AppUser): U {
        val s = repository.findByUserAndId(user, id)
            .orElseThrow {
                EntityNotFoundException(
                    String.format(
                        "Resource with id %d does not exist or is deleted.",
                        id
                    )
                )
            }
        return toDto(s)
    }

    override fun save(dto: U, user: AppUser): S {
        dto.user = user
        val model: S = toModel(dto)
        return repository.save(model)
    }

    override fun updateById(id: Long, dto: U, user: AppUser): S {
        dto.user = user
        val s = repository.findByUserAndId(user, id)
            .orElseThrow {
                EntityNotFoundException(
                    String.format(
                        "Resource with id %d does not exist or is deleted.",
                        id
                    )
                )
            }
        val model: S = toModel(dto)
        model.id = s.id
        return repository.save(s)
    }

    override fun deleteById(id: Long, user: AppUser) {
        repository.deleteByUserAndId(user, id)
    }

    override fun findAll(specification: V, pageable: Pageable, request: HttpServletRequest): PaginatedResponse<S> {
        val all = repository.findAll(specification, pageable)
        val data = all.content
            .stream()
            .map { source: S -> toDto(source) }
            .collect(Collectors.toUnmodifiableList())
        return PaginatedResponse(all, request, data)
    }

    override fun findAll(specification: V): List<U> {
        val sList = repository.findAll(specification)
        return sList.stream()
            .map { source: S -> toDto(source) }
            .collect(Collectors.toUnmodifiableList())
    }

    override fun findAll(specification: V, sort: Sort): List<U> {
        val sList = repository.findAll(specification, sort)
        return sList.stream()
            .map { source: S -> toDto(source) }
            .collect(Collectors.toUnmodifiableList())
    }
}