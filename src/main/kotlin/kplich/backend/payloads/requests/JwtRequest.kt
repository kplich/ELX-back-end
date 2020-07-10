package kplich.backend.payloads.requests

import java.io.Serializable


data class JwtRequest(val username: String, val password: String) : Serializable
