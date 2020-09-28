package com.g10.api.base.filters

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.g10.api.base.auth.AppUser
import com.g10.api.base.config.Settings
import com.g10.api.base.config.UserDetailsServiceImpl
import com.g10.api.base.error.Codes
import com.g10.api.base.error.Error
import com.g10.api.base.error.exceptions.TokenNotFoundException
import com.g10.api.base.renderers.ErrorResponse
import com.g10.api.base.utils.JWT
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.security.SignatureException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JWTRequestFilter(
    val userDetailsService: UserDetailsServiceImpl,
    val jwt: JWT,
    val pathMatcher: AntPathMatcher,
    val settings: Settings
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        filterChain: FilterChain
    ) {
        logger.info("Filter hit!")
        val header = Optional.ofNullable(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION))
        try {
            logger.info("checking for header$header")
            if (header.isPresent) {
                val token = header.get()
                    .substring(7)
                val username: Optional<String> = Optional.ofNullable(jwt.extractUsername(token))
                if (username.isPresent && ensureContextIsNull()) {
                    logger.warn("inside UPA")
                    val user: AppUser? = userDetailsService.loadUserByUsername(username.get())
                    val authenticationToken = UsernamePasswordAuthenticationToken(user, null, user?.authorities)
                    authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
                    SecurityContextHolder.getContext().authentication = authenticationToken
                    filterChain.doFilter(httpServletRequest, httpServletResponse)
                }
            } else throw TokenNotFoundException("Token not present in header.")
        } catch (e: MalformedJwtException) {
            logger.error(e)
            handleError(httpServletResponse, e)
        } catch (e: ExpiredJwtException) {
            logger.error(e)
            handleError(httpServletResponse, e)
        } catch (e: SignatureException) {
            logger.error(e)
            handleError(httpServletResponse, e)
        } catch (e: UsernameNotFoundException) {
            logger.error(e)
            handleError(httpServletResponse, e)
        } catch (e: TokenNotFoundException) {
            logger.error(e)
            handleError(httpServletResponse, e)
        }
    }

    @Throws(IOException::class)
    protected fun handleError(response: HttpServletResponse, exception: Exception) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.UNAUTHORIZED.value()
        if (exception is SignatureException) {
            response.writer
                .write(invalidToken(exception))
        } else if (exception is UsernameNotFoundException) {
            response.writer
                .write(userNotFound(exception))
        } else if (exception is ExpiredJwtException) {
            response.writer
                .write(expiredToken(exception))
        } else if (exception is TokenNotFoundException) {
            response.writer
                .write(tokenNotFound(exception))
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        logger.info(request.servletPath)
        return settings.skipPaths
            .stream()
            .anyMatch { s -> pathMatcher.match(s, request.servletPath) }
    }

    private fun ensureContextIsNull(): Boolean {
        val authentication = Optional.ofNullable(
            SecurityContextHolder.getContext()
                .authentication
        )
        return authentication.isEmpty
    }

    @Throws(JsonProcessingException::class)
    private fun tokenNotFound(e: TokenNotFoundException): String {
        val error = Error(e.localizedMessage, HttpStatus.UNAUTHORIZED, Codes.AUTH_TOKEN_NOT_FOUND)
        val errorResponse = ErrorResponse(error)
        val mapper = ObjectMapper()
        return mapper.writeValueAsString(errorResponse)
    }

    @Throws(JsonProcessingException::class)
    private fun invalidToken(e: SignatureException): String {
        val error = Error(e.localizedMessage, HttpStatus.UNAUTHORIZED, Codes.INVALID_AUTH_TOKEN)
        val errorResponse = ErrorResponse(error)
        val mapper = ObjectMapper()
        return mapper.writeValueAsString(errorResponse)
    }

    @Throws(JsonProcessingException::class)
    private fun expiredToken(e: ExpiredJwtException): String {
        val error = Error(e.localizedMessage, HttpStatus.UNAUTHORIZED, Codes.AUTH_TOKEN_EXPIRED)
        val errorResponse = ErrorResponse(error)
        val mapper = ObjectMapper()
        return mapper.writeValueAsString(errorResponse)
    }

    @Throws(JsonProcessingException::class)
    private fun userNotFound(e: UsernameNotFoundException): String {
        val error = Error(e.localizedMessage, HttpStatus.UNAUTHORIZED, Codes.USER_NOT_FOUND)
        val errorResponse = ErrorResponse(error)
        val mapper = ObjectMapper()
        return mapper.writeValueAsString(errorResponse)
    }
}