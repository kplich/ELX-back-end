package kplich.backend.payloads.requests

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class SignupRequest(
        @NotBlank @Size(min = 3, max = 20) val username: String,
        @NotBlank @Size(min = 8, max = 40) val password: String
)
