package kplich.backend.entities

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import javax.validation.constraints.*
import kotlin.reflect.KClass

@Entity
@Table(name = "conversations")
data class Conversation(
        @OneToOne
        var interestedUser: ApplicationUser,

        @ManyToOne
        var item: Item,

        @OneToMany(mappedBy = "conversation")
        var messages: MutableList<Message>,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0
)

@Entity
@Table(name = "messages")
data class Message(

        @ManyToOne
        var conversation: Conversation,

        @OneToOne
        var sendingUser: ApplicationUser,

        @get:NotNull
        @get:PastOrPresent
        var sentOn: LocalDateTime,

        @get:NotBlank
        @get:Size(max = MESSAGE_CONTENT_MAX_LENGTH, message = MESSAGE_TOO_LONG_MSG)
        var textContent: String,

        @OneToOne
        var offer: Offer,

        @Id
        @GeneratedValue
        var id: Long = 0
) {
    companion object {
        const val MESSAGE_CONTENT_MAX_LENGTH = 1000
        const val MESSAGE_TOO_LONG_MSG = "Message is too long."
    }
}

@AdvanceNoGreaterThanPrice(message = Offer.ADVANCE_GREATER_THAN_PRICE)
@Entity
@Table(name = "offers")
data class Offer(
        @OneToOne
        var message: Message,

        @Enumerated(EnumType.STRING)
        var type: OfferType,

        @get:NotNull(message = PRICE_REQUIRED_MSG)
        @get:DecimalMin(value = PRICE_MINIMUM, inclusive = true, message = PRICE_TOO_LOW_MSG)
        @get:DecimalMax(value = PRICE_MAXIMUM, inclusive = true, message = PRICE_TOO_HIGH_MSG)
        @get:Digits(integer = 9, fraction = 4, message = PRICE_TOO_PRECISE_MSG)
        var price: BigDecimal,

        @get:NotNull(message = PRICE_REQUIRED_MSG)
        @get:DecimalMin(value = PRICE_MINIMUM, inclusive = true, message = PRICE_TOO_LOW_MSG)
        @get:DecimalMax(value = PRICE_MAXIMUM, inclusive = true, message = PRICE_TOO_HIGH_MSG)
        @get:Digits(integer = 9, fraction = 4, message = PRICE_TOO_PRECISE_MSG)
        var advance: BigDecimal,

        @Enumerated(EnumType.STRING)
        var offerStatus: OfferStatus,

        var contractAddress: String?,

        @Id
        @GeneratedValue
        var id: Long = 0
) {
    companion object {
        const val PRICE_REQUIRED_MSG = "Price is required."
        const val PRICE_MINIMUM = "0.0"
        const val PRICE_TOO_LOW_MSG = "The lowest price allowed is 0 Ξ."
        const val PRICE_MAXIMUM = "100000000.0"
        const val PRICE_TOO_HIGH_MSG = "The highest price allowed is 100000000 Ξ."
        const val PRICE_TOO_PRECISE_MSG = "Price should have precision of 0.0001 Ξ."

        const val ADVANCE_GREATER_THAN_PRICE = "Advance cannot be greater than price."
    }
}

@Suppress("unused") // parameters required by constraint validation?
@Constraint(validatedBy = [AdvanceNoGreaterThanPriceValidator::class])
@Target(AnnotationTarget.CLASS)
annotation class AdvanceNoGreaterThanPrice(val message: String = "", val groups: Array<KClass<*>> = [], val payload: Array<KClass<out Payload>> = [])

class AdvanceNoGreaterThanPriceValidator : ConstraintValidator<AdvanceNoGreaterThanPrice, Offer> {
    override fun isValid(value: Offer, context: ConstraintValidatorContext): Boolean {
        return value.advance <= value.price
    }

}

enum class OfferStatus {
    AWAITING, ACCEPTED, DECLINED
}

enum class OfferType {
    PLAIN_ADVANCE
}
