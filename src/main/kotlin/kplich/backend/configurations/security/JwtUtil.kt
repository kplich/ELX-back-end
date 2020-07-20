package kplich.backend.configurations.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*

@Component
class JwtUtil(
        @Value("\${jwt.secret}") private val jwtSecret: String
) : Serializable {

    fun generateJwt(username: String, roles: List<String>): String {
        val key = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

        return Jwts.builder()
                .setSubject(username)
                .claim(CLAIM_ROLES_KEY, roles)
                .setIssuedAt(Date())
                .signWith(key)
                .compact()
    }

    fun parseJwtClaims(token: String): Claims {
        val key = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
    }

    companion object {
        const val CLAIM_SUBJECT_KEY = "sub"
        const val CLAIM_ROLES_KEY = "roles"
    }
}
