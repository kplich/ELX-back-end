package kplich.backend.dtos.requests

import java.io.Serializable


data class JwtRequest(val username: String, val password: String) : Serializable
