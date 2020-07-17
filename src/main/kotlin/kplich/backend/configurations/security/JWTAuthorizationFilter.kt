package kplich.backend.configurations.security

import io.jsonwebtoken.Jwts
import kplich.backend.configurations.security.SecurityConstants.HEADER_STRING
import kplich.backend.configurations.security.SecurityConstants.SECRET
import kplich.backend.configurations.security.SecurityConstants.TOKEN_PREFIX
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthorizationFilter(authManager: AuthenticationManager) : BasicAuthenticationFilter(authManager) {
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
            val token = Jwts.parserBuilder()
                    .setSigningKey(SECRET)
                    .build()
                    .parseClaimsJws(tokenHeader.replace(TOKEN_PREFIX, ""))
                    .body

            return if (token != null) {
                UsernamePasswordAuthenticationToken(
                        token.get("username", String::class.java),
                        null,
                        getRolesFromList(token.get("roles", List::class.java) as List<String>))
            }
            else {
                null
            }
        }
        return null
    }
}
