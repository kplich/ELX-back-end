package kplich.backend.payloads.requests

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class LoginRequest (
        @get:NotBlank val username: String,
        @get:NotBlank val password: String
)
