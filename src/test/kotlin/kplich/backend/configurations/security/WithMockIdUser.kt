package kplich.backend.configurations.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.test.context.support.WithSecurityContext
import org.springframework.security.test.context.support.WithSecurityContextFactory
import java.lang.annotation.Inherited
import java.util.*

/**
 * Code written after the example of [org.springframework.security.test.context.support.WithMockUserSecurityContextFactory] and [org.springframework.security.test.context.support.WithMockUser]
 */

class SecurityConfigFactory : WithSecurityContextFactory<WithMockIdUser> {

    override fun createSecurityContext(withUser: WithMockIdUser): SecurityContext {
        val username = withUser.username

        val grantedAuthorities: MutableList<GrantedAuthority> = ArrayList()
        for (authority in withUser.authorities) {
            grantedAuthorities.add(SimpleGrantedAuthority(authority))
        }

        if (grantedAuthorities.isEmpty()) {
            for (role in withUser.roles) {
                require(!role.startsWith("ROLE_")) {
                    "roles cannot start with ROLE_ Got $role"
                }
                grantedAuthorities.add(SimpleGrantedAuthority("ROLE_$role"))
            }
        } else check(withUser.roles.size == 1 && "USER" == withUser.roles[0]) {
            "You cannot define roles attribute ${listOf(withUser.roles)} with authorities attribute ${listOf<String>(*withUser.authorities)}"
        }

        val principal = User(username, withUser.password, true, true, true, true, grantedAuthorities)
        val authentication: Authentication = UsernamePasswordAuthenticationToken(
                principal, principal.password, principal.authorities).apply {
            details = withUser.id
        }
        val context = SecurityContextHolder.createEmptyContext()
        context.authentication = authentication
        return context
    }

}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@WithSecurityContext(factory = SecurityConfigFactory::class)
annotation class WithMockIdUser(
        val id: Long = 0,
        val username: String = "username",
        val password: String = "password",
        val roles: Array<String> = ["USER"],
        val authorities: Array<String> = []
)
