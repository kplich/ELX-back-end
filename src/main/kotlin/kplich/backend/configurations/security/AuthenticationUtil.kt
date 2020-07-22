package kplich.backend.configurations.security

import kplich.backend.entities.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

fun getAuthoritiesFromRoles(roles: Collection<Role>): Collection<GrantedAuthority> {
    return roles.map { role -> SimpleGrantedAuthority(role.name.name) }
}
