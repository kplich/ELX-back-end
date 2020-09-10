package kplich.backend.entities.conversation

import kplich.backend.entities.authentication.ApplicationUser
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
data class Message(

        @ManyToOne
        var conversation: Conversation,

        @OneToOne
        var sender: ApplicationUser,

        @get:NotNull
        @get:PastOrPresent
        var sentOn: LocalDateTime,

        @get:Size(max = MESSAGE_CONTENT_MAX_LENGTH, message = MESSAGE_TOO_LONG_MSG)
        var content: String,

        @OneToOne
        var offer: Offer? = null,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0
) {
    companion object {
        const val MESSAGE_CONTENT_MAX_LENGTH = 1000
        const val MESSAGE_TOO_LONG_MSG = "Message is too long."
        const val MESSAGE_REQUIRED_WITHOUT_OFFER = "If message offer is not provided, message is required"
    }
}

@Suppress("unused")
@Constraint(validatedBy = [MessageRequiredWithoutOfferValidator::class])
@Target(AnnotationTarget.CLASS)
annotation class MessageRequiredWithoutOffer(
        val message: String = Message.MESSAGE_REQUIRED_WITHOUT_OFFER,
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

class MessageRequiredWithoutOfferValidator: ConstraintValidator<MessageRequiredWithoutOffer, Message> {
    override fun isValid(value: Message, context: ConstraintValidatorContext): Boolean {
        return if(value.offer == null) {
            value.content.isNotBlank()
        } else {
            true
        }
    }
}