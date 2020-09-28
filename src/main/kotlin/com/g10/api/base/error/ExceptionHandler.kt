package com.g10.api.base.error

import com.g10.api.base.renderers.ErrorResponse
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.stream.Collectors
import javax.persistence.EntityNotFoundException

@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {
    private fun generateResponse(error: Error): ResponseEntity<Any> {
        return ResponseEntity(ErrorResponse(error), error.status)
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val message = ex.localizedMessage
            .split(":").toTypedArray()[0].trim()
        val error = Error(message, status, Codes.VALIDATION_ERROR)
        return generateResponse(error)
    }

    //other exception handlers below
    @ExceptionHandler(EntityNotFoundException::class)
    protected fun handleEntityNotFound(exception: EntityNotFoundException): ResponseEntity<Any> {
        val error = Error(
            exception.localizedMessage,
            HttpStatus.NOT_FOUND, Codes.NOT_FOUND
        )
        return generateResponse(error)
    }

    @ExceptionHandler(EmptyResultDataAccessException::class)
    protected fun handleNotFound(exception: EmptyResultDataAccessException): ResponseEntity<Any> {
        val error = Error(
            exception.localizedMessage,
            HttpStatus.NOT_FOUND, Codes.NOT_FOUND
        )
        return generateResponse(error)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val fieldErrors = ex.bindingResult
            .fieldErrors
            .stream()
            .map { error: org.springframework.validation.FieldError ->
                FieldError(
                    error.field,
                    error.defaultMessage
                )
            }
            .collect(Collectors.toUnmodifiableList())
        val message = String.format("%d field errors.", fieldErrors.size)
        val error = Error(message, HttpStatus.BAD_REQUEST, Codes.VALIDATION_ERROR, fieldErrors)
        return generateResponse(error)
    }

    @ExceptionHandler(BadCredentialsException::class)
    protected fun handleLoginFail(exception: BadCredentialsException): ResponseEntity<Any> {
        val error = Error(exception.localizedMessage, HttpStatus.UNAUTHORIZED, Codes.AUTH_FAILED)
        return generateResponse(error)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    protected fun handleConstrainViolation(exception: ConstraintViolationException): ResponseEntity<Any> {
        val message: String = exception.cause!!.message!!.split("\n")[1].trim()

        val error = Error(message, HttpStatus.BAD_REQUEST, Codes.VALIDATION_ERROR)
        return generateResponse(error)
    }

    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val error = Error(ex.localizedMessage, HttpStatus.INTERNAL_SERVER_ERROR, Codes.INTERNAL_ERROR)
        return generateResponse(error)
    }

    override fun handleHttpRequestMethodNotSupported(
        ex: HttpRequestMethodNotSupportedException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val error = Error(ex.localizedMessage, HttpStatus.METHOD_NOT_ALLOWED, Codes.INVALID_METHOD)
        return generateResponse(error)
    }
}