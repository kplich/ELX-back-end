package kplich.backend.payloads.requests

import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class SignupRequest(
        @get:Size(min = 3, max = 20)
        @get:Pattern(regexp = "\\w*")
        val username: String,

        @get:Size(min = 8, max = 40)
        @get:Pattern.List(value = [
            Pattern(regexp = ".*[A-Z]+.*"),
            Pattern(regexp = ".*[a-z]+.*"),
            Pattern(regexp = ".*\\d+.*"),
            Pattern(regexp = ".*[\\W_]+.*")
        ])
        val password: String
)
