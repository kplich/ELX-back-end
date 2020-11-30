package kplich.backend.authentication.payloads.requests

import io.swagger.annotations.ApiModel
import kplich.backend.authentication.payloads.requests.PasswordChangeRequest.Companion.PASSWORDS_MUSTNT_MATCH
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
import kotlin.reflect.KClass

@ApiModel(description = "a request for changing the user's password")
@PasswordsNotEqual(message = PASSWORDS_MUSTNT_MATCH)
data class PasswordChangeRequest(
        @get:NotBlank(message = OLD_PASSWORD_REQUIRED)
        val oldPassword: String,

        @get:NotBlank(message = NEW_PASSWORD_REQUIRED)
        @get:Size(min = 8, max = 40, message = PASSWORD_MUST_BE_BETWEEN_8_AND_40)
        @get:Pattern.List(value = [
            Pattern(regexp = ".*[A-Z]+.*", message = PASSWORD_MUST_HAVE_UPPERCASE_LETTER),
            Pattern(regexp = ".*[a-z]+.*", message = PASSWORD_MUST_HAVE_LOWERCASE_LETTER),
            Pattern(regexp = ".*\\d+.*", message = PASSWORD_MUST_HAVE_DIGIT),
            Pattern(regexp = ".*[\\W_]+.*", message = PASSWORD_MUST_HAVE_SPECIAL_CHARACTER)
        ])
        val newPassword: String
) {
    companion object {
        const val PASSWORDS_MUSTNT_MATCH = "Old password and new password mustn't match."
        const val OLD_PASSWORD_REQUIRED = "Old password is required."
        const val NEW_PASSWORD_REQUIRED = "New password is required."
        const val PASSWORD_MUST_BE_BETWEEN_8_AND_40 = "New password must be at least 8 and at most 40 characters long."
        const val PASSWORD_MUST_HAVE_UPPERCASE_LETTER = "New password must contain an uppercase letter."
        const val PASSWORD_MUST_HAVE_LOWERCASE_LETTER = "New password must contain a lowercase letter."
        const val PASSWORD_MUST_HAVE_DIGIT = "New password must contain a digit."
        const val PASSWORD_MUST_HAVE_SPECIAL_CHARACTER = "New password must contain a special character."
    }
}

@Suppress("unused") // parameters required by constraint validation?
@Constraint(validatedBy = [PasswordChangeRequestValidator::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class PasswordsNotEqual(val message: String = "", val groups: Array<KClass<*>> = [], val payload: Array<KClass<out Payload>> = [])

class PasswordChangeRequestValidator : ConstraintValidator<PasswordsNotEqual, PasswordChangeRequest> {
    override fun isValid(request: PasswordChangeRequest, context: ConstraintValidatorContext): Boolean {
        return request.oldPassword != request.newPassword
    }
}
