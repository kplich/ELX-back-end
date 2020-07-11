package kplich.backend.configurations.security

import org.springframework.security.core.Authentication

fun getRoles(authentication: Authentication): List<String> {
    val roles = mutableListOf<String>()
    authentication.authorities.forEach {
        roles.add(it.authority)
    }
    return roles
}
