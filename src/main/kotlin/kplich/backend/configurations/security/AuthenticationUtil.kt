package kplich.backend.configurations.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

fun getRolesFromAuthentication(authentication: Authentication): List<String> {
    val roles = mutableListOf<String>()
    authentication.authorities.forEach {
        roles.add(it.authority)
    }
    return roles
}

fun getRolesFromList(roles: List<String>): Collection<GrantedAuthority> {
    return roles.map { string -> SimpleGrantedAuthority(string) }
}
