package kplich.backend.conversation.entities

import kplich.backend.authentication.entities.ApplicationUser
import kplich.backend.conversation.entities.Message.Companion.CONTENT_AND_OFFER_NULL_MESSAGE
import kplich.backend.conversation.entities.offer.Offer
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import javax.validation.constraints.*
import kotlin.reflect.KClass

@Entity
@Table(name = "messages")
@EitherContentOrOfferMustBeNotNull(message = CONTENT_AND_OFFER_NULL_MESSAGE)
data class Message(

        @ManyToOne
        var conversation: Conversation,

        @OneToOne
        var sender: ApplicationUser,

        @get:NotNull
        @get:PastOrPresent
        var sentOn: LocalDateTime,

        @get:Size(max = MESSAGE_CONTENT_MAX_LENGTH, message = MESSAGE_TOO_LONG_MSG)
        var content: String? = null,

        @OneToOne
        var offer: Offer? = null,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0
) {
    companion object {
        const val MESSAGE_CONTENT_MAX_LENGTH = 1000
        const val MESSAGE_TOO_LONG_MSG = "Message is too long."
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
    : ConstraintValidator<EitherContentOrOfferMustBeNotNull, Message> {
    override fun isValid(value: Message, context: ConstraintValidatorContext): Boolean {
        println(value.content)
        println(value.offer)
        return with(value) {
            !(content == null && offer == null)
        }
    }
}
