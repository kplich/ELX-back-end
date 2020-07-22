package kplich.backend.configurations.security

import com.fasterxml.jackson.databind.ObjectMapper
import kplich.backend.configurations.security.SecurityConstants.BEARER
import kplich.backend.payloads.requests.LoginRequest
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationFilter(
        @Lazy authenticationManager: AuthenticationManager,
        private val jwtUtil: JwtUtil) : UsernamePasswordAuthenticationFilter() {
    init {
        this.authenticationManager = authenticationManager
    }

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
            request: HttpServletRequest, response: HttpServletResponse): Authentication {
        return try {
            val loginRequest: LoginRequest = ObjectMapper().readValue(request.inputStream, LoginRequest::class.java)
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                            loginRequest.username, loginRequest.password))
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun successfulAuthentication(
            request: HttpServletRequest,
            response: HttpServletResponse,
            chain: FilterChain,
            auth: Authentication) {
        val token = jwtUtil.generateJwt(auth.name)

        response.addHeader(HttpHeaders.AUTHORIZATION, "$BEARER $token")
    }

}
