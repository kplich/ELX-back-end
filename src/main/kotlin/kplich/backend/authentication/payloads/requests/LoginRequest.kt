package kplich.backend.authentication.payloads.requests

import io.swagger.annotations.ApiModel
import javax.validation.constraints.NotBlank

@ApiModel(value = "Log-in Request", description = "request for logging in")
data class LoginRequest (
        @get:NotBlank(message = USERNAME_REQUIRED)
        val username: String = "",

        @get:NotBlank(message = PASSWORD_REQUIRED)
        val password: String = ""
) {

    companion object {
        const val USERNAME_REQUIRED = "Username is required."
        const val PASSWORD_REQUIRED = "Password is required."
    }
}
