package com.g10.api.base.renderers

import com.g10.api.base.base.dto.BaseDTO
import com.g10.api.base.base.model.AppBaseModel
import org.springframework.core.MethodParameter
import org.springframework.data.domain.Page
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import javax.servlet.http.HttpServletRequest

class ErrorResponse<T>(val error: T)

class SuccessResponse<T>(val data: T)

@ControllerAdvice
class SuccessResponseAdvice<T> : ResponseBodyAdvice<Any> {
    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        return if (returnType.containingClass
                .isAnnotationPresent(RestController::class.java) && body !is ErrorResponse<*> && body !is PaginatedResponse<*>
        ) {
            SuccessResponse(body)
        } else body
    }
}

class PaginatedResponse<S : AppBaseModel?>(pagedData: Page<S>, request: HttpServletRequest, data: List<BaseDTO?>) {
    private val page: Int
    private val nextPage: Number?
    private val previousPage: Number?
    private val count: Int
    private val maxPages: Int
    private val totalCount: Long
    private val nextPageLink: String?
    private val previousPageLink: String?
    private val data: List<BaseDTO?>

    fun getNextPageable(pagedData: Page<S>): Number? {
        return if (pagedData.hasNext()) {
            pagedData.nextPageable()
                .pageNumber + 1
        } else {
            null
        }
    }

    fun getPreviousPageable(pagedData: Page<S>): Number? {
        return if (pagedData.hasPrevious()) {
            pagedData.previousPageable()
                .pageNumber + 1
        } else {
            null
        }
    }

    fun getNextPageLink(pagedData: Page<S>, request: HttpServletRequest): String? {
        return if (pagedData.hasNext()) {
            String.format(
                "%s?page=%d&size=%d",
                request.requestURL
                    .toString(),
                pagedData.nextOrLastPageable()
                    .pageNumber + 1, pagedData.size
            )
        } else {
            null
        }
    }

    fun getPreviousPageLink(pagedData: Page<S>, request: HttpServletRequest): String? {
        return if (pagedData.hasPrevious()) {
            String.format(
                "%s?page=%d&size=%d",
                request.requestURL
                    .toString(),
                pagedData.previousOrFirstPageable()
                    .pageNumber + 1, pagedData.size
            )
        } else {
            null
        }
    }

    init {
        page = pagedData.pageable.pageNumber + 1
        nextPage = getNextPageable(pagedData)
        previousPage = getPreviousPageable(pagedData)
        count = pagedData.numberOfElements
        maxPages = pagedData.totalPages
        totalCount = pagedData.totalElements
        nextPageLink = getNextPageLink(pagedData, request)
        previousPageLink = getPreviousPageLink(pagedData, request)
        this.data = data
    }
}