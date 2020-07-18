package kplich.backend.configurations.security

import kplich.backend.configurations.security.SecurityConstants.HEADER_STRING
import kplich.backend.configurations.security.SecurityConstants.TOKEN_PREFIX
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JWTAuthorizationFilter(authManager: AuthenticationManager) : BasicAuthenticationFilter(authManager) {

    private val jwtUtil: JwtUtil = JwtUtil()

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  chain: FilterChain) {
        val header = request.getHeader(HEADER_STRING)

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response)
            return
        }

        val authentication = getAuthentication(request)

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }

    private fun getAuthentication(request: HttpServletRequest): Authentication? {
        val tokenHeader = request.getHeader(HEADER_STRING)
        if (tokenHeader != null) {

            return try {
                val claims = jwtUtil.parseJwtClaims(tokenHeader.replace(TOKEN_PREFIX, ""))
                val username = claims["username"] as String

                @Suppress("UNCHECKED_CAST")
                val roles = claims["roles"] as List<String>

                UsernamePasswordAuthenticationToken(
                        username, null, getAuthoritiesFromStrings(roles))
            } catch(e: Exception) {
                logger.error("Error while parsing JWT claims.", e)
                null
            }
        }
        return null
    }
}
