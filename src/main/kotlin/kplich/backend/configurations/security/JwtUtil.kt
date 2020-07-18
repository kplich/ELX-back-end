package kplich.backend.configurations.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*

@Component
class JwtUtil : Serializable {

    @Value("\${jwt.secret}")
    private val jwtSecret: String = "secret"

    fun generateJwt(username: String, roles: List<String>): String {
        val key = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(Date())
                .signWith(key)
                .compact()
    }

    fun parseJwtClaims(token: String): Claims {
        val key = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
    }

    companion object {
        private val logger = LoggerFactory.getLogger(JwtUtil::class.java)
    }
}
