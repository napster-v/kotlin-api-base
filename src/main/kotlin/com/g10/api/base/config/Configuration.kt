package com.g10.api.base.config

import com.g10.api.base.filters.JWTRequestFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.util.AntPathMatcher
import org.springframework.web.cors.CorsConfiguration

@Configuration(value = "settings")
class Settings {
    val skipPaths: List<String> = listOf("/user/**", "/actuator/**")

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun pathMatcher(): AntPathMatcher {
        return AntPathMatcher()
    }

    fun getAuthenticatedUser(): Any? {
        val authentication = SecurityContextHolder.getContext()
            .authentication
        return if (authentication !is AnonymousAuthenticationToken) {
            authentication.principal
        } else null
    }
}

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration(
    service: UserDetailsServiceImpl,
    passwordEncoder: BCryptPasswordEncoder,
    jwtRequestFilter: JWTRequestFilter
) : WebSecurityConfigurerAdapter() {
    private val service: UserDetailsServiceImpl
    private val passwordEncoder: BCryptPasswordEncoder
    private val jwtRequestFilter: JWTRequestFilter

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors()
            .configurationSource { corsConfiguration() }
            .and()
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .deny()
            .and()
            .authorizeRequests()
            .antMatchers("/user/**", "/actuator/**")
            .permitAll()
            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.addFilterBefore(
            jwtRequestFilter,
            UsernamePasswordAuthenticationFilter::class.java
        )
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService<UserDetailsService>(service)
            .passwordEncoder(passwordEncoder)
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun corsConfiguration(): CorsConfiguration {
        val config = CorsConfiguration().applyPermitDefaultValues()
        config.addAllowedMethod(HttpMethod.OPTIONS)
        config.addAllowedMethod(HttpMethod.PUT)
        config.addAllowedMethod(HttpMethod.DELETE)
        return config
    }

    init {
        this.service = service
        this.passwordEncoder = passwordEncoder
        this.jwtRequestFilter = jwtRequestFilter
    }
}