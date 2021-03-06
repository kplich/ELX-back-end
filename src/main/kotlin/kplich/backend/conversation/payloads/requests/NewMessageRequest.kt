package kplich.backend.conversation.payloads.requests

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import kplich.backend.conversation.entities.Message
import kplich.backend.conversation.payloads.requests.NewMessageRequest.Companion.CONTENT_AND_OFFER_NULL_MESSAGE
import kplich.backend.conversation.payloads.requests.offer.NewOfferRequest
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import javax.validation.constraints.Size
import kotlin.reflect.KClass

@ApiModel(description = "Request for sending a new message")
@EitherContentOrOfferMustBeNotNull(message = CONTENT_AND_OFFER_NULL_MESSAGE)
data class NewMessageRequest(
        @ApiModelProperty(value = "text of the message", required = true)
        @get:Size(max = Message.MESSAGE_CONTENT_MAX_LENGTH, message = Message.MESSAGE_TOO_LONG_MSG)
        val content: String?,
        @ApiModelProperty(value = "offer attached to a message", required = false)
        val offer: NewOfferRequest? = null
) {
        companion object {
                const val CONTENT_AND_OFFER_NULL_MESSAGE = "Either message content or offer must not be null."
        }
}

@Suppress("unused") // parameters required by constraint validation?
@Constraint(validatedBy = [EitherContentOrOfferMustBeNotNullValidator::class])
@Target(AnnotationTarget.CLASS)
annotation class EitherContentOrOfferMustBeNotNull(
        val message: String = "",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

class EitherContentOrOfferMustBeNotNullValidator
        : ConstraintValidator<EitherContentOrOfferMustBeNotNull, NewMessageRequest> {
        override fun isValid(value: NewMessageRequest, context: ConstraintValidatorContext): Boolean {
                return with(value) {
                        !(content == null && offer == null)
                }
        }
}
