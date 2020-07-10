package kplich.backend.configurations.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable
import java.time.Duration
import java.util.*

@Component
class JwtUtil : Serializable {

    @Value("\${jwt.secret}")
    private val jwtSecret: String? = null

    private val jwtExpirationMs = Duration.ofMinutes(10).toMillis()

    fun generateJwtToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserDetailsImpl
        return Jwts.builder()
                .setSubject(userPrincipal.username) //				.claim("roles", String.valueOf(userPrincipal.getAuthorities()))
                .setIssuedAt(Date())
                .setExpiration(Date(Date().time + jwtExpirationMs))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)), SignatureAlgorithm.HS512)
                .compact()
    }

    fun getUserNameFromJwtToken(token: String?): String {
        return Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token).body.subject
    }

    fun validateJwtToken(authToken: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(authToken)
            return true
        } catch (e: SecurityException) {
            System.err.println("Invalid JWT signature: ${e.message}")
        } catch (e: MalformedJwtException) {
            System.err.println("Invalid JWT token: ${e.message}")
        } catch (e: UnsupportedJwtException) {
            System.err.println("JWT token is unsupported: ${e.message}")
        } catch (e: IllegalArgumentException) {
            System.err.println("JWT claims string is empty: ${e.message}")
        }
        return false
    }


}
