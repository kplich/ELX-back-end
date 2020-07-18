package kplich.backend.payloads.requests

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class SignupRequest(
        @get:NotBlank(message = USERNAME_REQUIRED)
        @get:Size(min = 3, max = 20, message = USERNAME_MUST_BE_BETWEEN_3_AND_20)
        @get:Pattern(regexp = "\\w*", message = USERNAME_NOT_IN_PATTERN)
        val username: String,

        @get:NotBlank(message = PASSWORD_REQUIRED)
        @get:Size(min = 8, max = 40, message = PASSWORD_MUST_BE_BETWEEN_8_AND_40)
        @get:Pattern.List(value = [
            Pattern(regexp = ".*[A-Z]+.*", message = PASSWORD_MUST_HAVE_UPPERCASE_LETTER),
            Pattern(regexp = ".*[a-z]+.*", message = PASSWORD_MUST_HAVE_LOWERCASE_LETTER),
            Pattern(regexp = ".*\\d+.*", message = PASSWORD_MUST_HAVE_DIGIT),
            Pattern(regexp = ".*[\\W_]+.*", message = PASSWORD_MUST_HAVE_SPECIAL_CHARACTER)
        ])
        val password: String
) {
    companion object {
        const val USERNAME_REQUIRED = "Username is required."
        const val USERNAME_MUST_BE_BETWEEN_3_AND_20 = "Username must be at least 3 and at most 20 characters long."
        const val USERNAME_NOT_IN_PATTERN = "Username may consist only of letters, numbers and underscores."

        const val PASSWORD_REQUIRED = "Password is required."
        const val PASSWORD_MUST_BE_BETWEEN_8_AND_40 = "Password must be at least 8 and at most 40 characters long."
        const val PASSWORD_MUST_HAVE_UPPERCASE_LETTER = "Password must contain an uppercase letter."
        const val PASSWORD_MUST_HAVE_LOWERCASE_LETTER = "Password must contain a lowercase letter."
        const val PASSWORD_MUST_HAVE_DIGIT = "Password must contain a digit."
        const val PASSWORD_MUST_HAVE_SPECIAL_CHARACTER = "Password must contain a special character."
    }
}
