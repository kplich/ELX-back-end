package kplich.backend.entities.conversation

import kplich.backend.configurations.PricePrecisionConstants
import kplich.backend.entities.authentication.ApplicationUser
import kplich.backend.entities.items.Item
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull
import kotlin.reflect.KClass

@AdvanceNoGreaterThanPrice(message = Offer.ADVANCE_GREATER_THAN_PRICE)
@Entity
@Table(name = "offers")
data class Offer(

        @OneToOne(mappedBy = "offer")
        var message: Message,

        @Enumerated(EnumType.STRING)
        var type: OfferType,

        @get:NotNull(message = PricePrecisionConstants.PRICE_REQUIRED_MSG)
        @get:DecimalMin(value = PricePrecisionConstants.PRICE_MINIMUM_STRING, inclusive = true, message = PricePrecisionConstants.PRICE_TOO_LOW_MSG)
        @get:DecimalMax(value = PricePrecisionConstants.PRICE_MAXIMUM_STRING, inclusive = true, message = PricePrecisionConstants.PRICE_TOO_HIGH_MSG)
        @get:Digits(integer = PricePrecisionConstants.PRICE_INTEGER_PART, fraction = PricePrecisionConstants.PRICE_DECIMAL_PART, message = PricePrecisionConstants.PRICE_TOO_PRECISE_MSG)
        var price: BigDecimal,

        @get:NotNull(message = PricePrecisionConstants.ADVANCE_REQUIRED_MSG)
        @get:DecimalMin(value = PricePrecisionConstants.PRICE_MINIMUM_STRING, inclusive = true, message = PricePrecisionConstants.ADVANCE_TOO_LOW_MSG)
        @get:DecimalMax(value = PricePrecisionConstants.PRICE_MAXIMUM_STRING, inclusive = true, message = PricePrecisionConstants.ADVANCE_TOO_HIGH_MSG)
        @get:Digits(integer = PricePrecisionConstants.PRICE_INTEGER_PART, fraction = PricePrecisionConstants.PRICE_DECIMAL_PART, message = PricePrecisionConstants.ADVANCE_TOO_PRECISE_MSG)
        var advance: BigDecimal,

        @Enumerated(EnumType.STRING)
        var offerStatus: OfferStatus = OfferStatus.AWAITING,

        var contractAddress: String? = null,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0
) {

    @get:Transient
    val accepted
        get(): Boolean = this.offerStatus == OfferStatus.ACCEPTED

    @get:Transient
    val declined
        get(): Boolean = this.offerStatus == OfferStatus.DECLINED

    @get:Transient
    val cancelled
        get(): Boolean = this.offerStatus == OfferStatus.CANCELLED

    @get:Transient
    val awaiting
        get(): Boolean = this.offerStatus == OfferStatus.AWAITING

    @get:Transient
    val sender
        get(): ApplicationUser = this.message.sender

    @get:Transient
    val conversation
        get(): Conversation = this.message.conversation

    @get:Transient
    val item
        get(): Item = this.conversation.item

    fun cancel(): Offer {
        if(awaiting) {
            this.offerStatus = OfferStatus.CANCELLED
        } else {
            throw IllegalStateException("Cannot cancel accepted/denied/cancelled offer")
        }

        return this
    }

    fun accept(contractAddress: String): Offer {
        if (awaiting) {
            this.contractAddress = contractAddress
            this.offerStatus = OfferStatus.ACCEPTED
        } else {
            throw IllegalStateException("Cannot accept accepted/denied/cancelled offer")
        }

        return this
    }

    fun decline(): Offer {
        if (awaiting) {
            this.offerStatus = OfferStatus.DECLINED
        } else {
            throw IllegalStateException("Cannot decline accepted/denied/cancelled offer")
        }

        return this
    }

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
    AWAITING, CANCELLED, ACCEPTED, DECLINED
}

enum class OfferType {
    PLAIN_ADVANCE
}