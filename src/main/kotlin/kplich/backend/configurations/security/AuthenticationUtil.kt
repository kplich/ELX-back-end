package kplich.backend.configurations.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

/**
 * Converts authorities in Authentication to list of strings
 */
fun Authentication.getRoles(): List<String> {
    val roles = mutableListOf<String>()
    authorities.forEach {
        roles.add(it.authority)
    }
    return roles
}

fun getRolesFromList(roles: List<String>): Collection<GrantedAuthority> {
    return roles.map { string -> SimpleGrantedAuthority(string) }
}
