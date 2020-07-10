package kplich.backend.payloads.responses

data class JwtResponse(
        val jwtToken: String,
        val username: String
)
