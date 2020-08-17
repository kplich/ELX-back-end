package kplich.backend.entities

import kplich.backend.configurations.PricePrecisionConstants.ADVANCE_REQUIRED_MSG
import kplich.backend.configurations.PricePrecisionConstants.ADVANCE_TOO_HIGH_MSG
import kplich.backend.configurations.PricePrecisionConstants.ADVANCE_TOO_LOW_MSG
import kplich.backend.configurations.PricePrecisionConstants.ADVANCE_TOO_PRECISE_MSG
import kplich.backend.configurations.PricePrecisionConstants.PRICE_DECIMAL_PART
import kplich.backend.configurations.PricePrecisionConstants.PRICE_INTEGER_PART
import kplich.backend.configurations.PricePrecisionConstants.PRICE_MAXIMUM_STRING
import kplich.backend.configurations.PricePrecisionConstants.PRICE_MINIMUM_STRING
import kplich.backend.configurations.PricePrecisionConstants.PRICE_REQUIRED_MSG
import kplich.backend.configurations.PricePrecisionConstants.PRICE_TOO_HIGH_MSG
import kplich.backend.configurations.PricePrecisionConstants.PRICE_TOO_LOW_MSG
import kplich.backend.configurations.PricePrecisionConstants.PRICE_TOO_PRECISE_MSG
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
@Table(name = "conversations", indexes = [Index(columnList = "interested_user_id, item_id", unique = true)])
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
        var sender: ApplicationUser,

        @get:NotNull
        @get:PastOrPresent
        var sentOn: LocalDateTime,

        @get:NotBlank
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
    }
}

@AdvanceNoGreaterThanPrice(message = Offer.ADVANCE_GREATER_THAN_PRICE)
@Entity
@Table(name = "offers")
data class Offer(

        @OneToOne(mappedBy = "offer")
        var message: Message,

        @Enumerated(EnumType.STRING)
        var type: OfferType,

        @get:NotNull(message = PRICE_REQUIRED_MSG)
        @get:DecimalMin(value = PRICE_MINIMUM_STRING, inclusive = true, message = PRICE_TOO_LOW_MSG)
        @get:DecimalMax(value = PRICE_MAXIMUM_STRING, inclusive = true, message = PRICE_TOO_HIGH_MSG)
        @get:Digits(integer = PRICE_INTEGER_PART, fraction = PRICE_DECIMAL_PART, message = PRICE_TOO_PRECISE_MSG)
        var price: BigDecimal,

        @get:NotNull(message = ADVANCE_REQUIRED_MSG)
        @get:DecimalMin(value = PRICE_MINIMUM_STRING, inclusive = true, message = ADVANCE_TOO_LOW_MSG)
        @get:DecimalMax(value = PRICE_MAXIMUM_STRING, inclusive = true, message = ADVANCE_TOO_HIGH_MSG)
        @get:Digits(integer = PRICE_INTEGER_PART, fraction = PRICE_DECIMAL_PART, message = ADVANCE_TOO_PRECISE_MSG)
        var advance: BigDecimal,

        @Enumerated(EnumType.STRING)
        var offerStatus: OfferStatus = OfferStatus.AWAITING,

        var contractAddress: String? = null,

        @Id
        @GeneratedValue
        var id: Long = 0
) {
    companion object {
        const val ADVANCE_GREATER_THAN_PRICE = "Advance cannot be greater than price."
    }
}

@Suppress("unused") // parameters required by constraint validation?
@Constraint(validatedBy = [AdvanceNoGreaterThanPriceValidator::class])
@Target(AnnotationTarget.CLASS)
annotation class AdvanceNoGreaterThanPrice(
        val message: String = "",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

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
