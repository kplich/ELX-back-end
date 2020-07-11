package kplich.backend.configurations.security

import kplich.backend.services.UserDetailsServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthTokenFilter(
        private val jwtUtil: JwtUtil,
        private val userDetailsService: UserDetailsServiceImpl) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            val jwt = extractJwtFromRequest(request)
            if (jwt != null && jwtUtil.validateJwtToken(jwt)) {
                val username: String = jwtUtil.getUserNameFromJwtToken(jwt)
                val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)
                val authentication = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
                response.addHeader("new_token", jwtUtil.generateJwt(username, getRoles(SecurityContextHolder.getContext().authentication)))
            }
        } catch (e: Exception) {
            Companion.logger.error("Cannot set user authentication: {}", e.message)
        }
        filterChain.doFilter(request, response)
    }

    private fun extractJwtFromRequest(request: HttpServletRequest): String? {
        val authorizationHeader = request.getHeader("Authorization")
        return if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(BEARER_PREFIX)) {
            authorizationHeader.substring(7, authorizationHeader.length)
        } else null
    }

    companion object {
        private val BEARER_PREFIX = "Bearer "
        private val logger = LoggerFactory.getLogger(AuthTokenFilter::class.java)
    }
}
