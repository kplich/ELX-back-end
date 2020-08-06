package kplich.backend.payloads.requests.authentication

import javax.validation.constraints.NotBlank

data class LoginRequest (
        @get:NotBlank(message = USERNAME_REQUIRED) val username: String = "",
        @get:NotBlank(message = PASSWORD_REQUIRED) val password: String = ""
) {

    companion object {
        const val USERNAME_REQUIRED = "Username is required."
        const val PASSWORD_REQUIRED = "Password is required."
    }
}
