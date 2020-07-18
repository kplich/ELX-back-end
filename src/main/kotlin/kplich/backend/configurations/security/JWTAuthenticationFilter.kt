package kplich.backend.configurations.security

import com.fasterxml.jackson.databind.ObjectMapper
import kplich.backend.configurations.security.SecurityConstants.HEADER_STRING
import kplich.backend.configurations.security.SecurityConstants.TOKEN_PREFIX
import kplich.backend.entities.ApplicationUser
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter(authManager: AuthenticationManager) : UsernamePasswordAuthenticationFilter() {
    private val jwtUtil = JwtUtil()

    init {
        authenticationManager = authManager
    }

    @Throws(AuthenticationException::class, IOException::class, ServletException::class)
    override fun attemptAuthentication(
            req: HttpServletRequest, res: HttpServletResponse): Authentication {
        val user = ObjectMapper()
                .readValue(req.inputStream, ApplicationUser::class.java)
        return authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        user.username,
                        user.password)
                )
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
            req: HttpServletRequest,
            res: HttpServletResponse, chain: FilterChain?,
            auth: Authentication) {
        val jwt = jwtUtil.generateJwt(auth.name, auth.getRoles())
        res.addHeader(HEADER_STRING, "$TOKEN_PREFIX $jwt")
    }
}

