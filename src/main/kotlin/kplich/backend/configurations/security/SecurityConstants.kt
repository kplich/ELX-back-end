package kplich.backend.configurations.security

import org.springframework.beans.factory.annotation.Value

object SecurityConstants {
    @Value("\${jwt.secret}")
    const val SECRET = "secret"
    const val TOKEN_PREFIX = "Bearer "
    const val HEADER_STRING = "Authorization"
}
