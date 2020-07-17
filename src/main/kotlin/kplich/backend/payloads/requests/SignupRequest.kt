package kplich.backend.payloads.requests

import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class SignupRequest(
        @get:NotNull(message = "Username is required.")
        @get:Size(min = 3, max = 20, message = "Username must be at least 3 and at most 20 characters long.")
        @get:Pattern(regexp = "\\w*", message = "Username may consist only of letters, numbers and underscores.")
        val username: String,

        @get:NotNull(message = "Password is required.")
        @get:Size(min = 8, max = 40, message = "Password must be at least 8 and at most 40 characters long.")
        @get:Pattern.List(value = [
            Pattern(regexp = ".*[A-Z]+.*", message = "Password must contain a capital letter."),
            Pattern(regexp = ".*[a-z]+.*", message = "Password must contain a lowercase letter."),
            Pattern(regexp = ".*\\d+.*", message = "Password must contain a digit."),
            Pattern(regexp = ".*[\\W_]+.*", message = "Password must contain a special character.")
        ])
        val password: String
)
