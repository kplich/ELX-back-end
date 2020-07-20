package kplich.backend.configurations.security

import kplich.backend.configurations.security.JwtUtil.Companion.CLAIM_ROLES_KEY
import kplich.backend.configurations.security.JwtUtil.Companion.CLAIM_SUBJECT_KEY
import kplich.backend.configurations.security.SecurityConstants.AUTHORIZATION
import kplich.backend.configurations.security.SecurityConstants.BEARER
import org.springframework.context.annotation.Lazy
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
class JWTAuthorizationFilter(
        @Lazy authManager: AuthenticationManager, // WebSecurity both creates this bean and depends on it
        private val jwtUtil: JwtUtil
) : BasicAuthenticationFilter(authManager) {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  chain: FilterChain) {
        val header = request.getHeader(AUTHORIZATION)

        if (header == null || !header.startsWith(BEARER)) {
            chain.doFilter(request, response)
            return
        }

        val authentication = getAuthentication(header)

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }

    private fun getAuthentication(header: String): Authentication? {
        return try {
            val claims = jwtUtil.parseJwtClaims(header.replace(BEARER, "").trim())
            val username = claims[CLAIM_SUBJECT_KEY] as String

            @Suppress("UNCHECKED_CAST")
            val roles = claims[CLAIM_ROLES_KEY] as List<String>

            UsernamePasswordAuthenticationToken(
                    username, null, getAuthoritiesFromStrings(roles))
        } catch (e: Exception) {
            logger.error("Error while parsing JWT claims.", e)
            null
        }
    }
}
