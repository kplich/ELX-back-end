package kplich.backend.entities.conversation.offer

import kplich.backend.configurations.PricePrecisionConstants
import kplich.backend.entities.conversation.Message
import kplich.backend.entities.conversation.offer.PlainAdvanceOffer.Companion.ADVANCE_GREATER_THAN_PRICE
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull
import kotlin.reflect.KClass

@AdvanceNoGreaterThanPrice(message = ADVANCE_GREATER_THAN_PRICE)
@Entity
@Table(name = "plain_advance_offers")
class PlainAdvanceOffer(
        @get:NotNull(message = PricePrecisionConstants.ADVANCE_REQUIRED_MSG)
        @get:DecimalMin(value = PricePrecisionConstants.PRICE_MINIMUM_STRING, inclusive = true, message = PricePrecisionConstants.ADVANCE_TOO_LOW_MSG)
        @get:DecimalMax(value = PricePrecisionConstants.PRICE_MAXIMUM_STRING, inclusive = true, message = PricePrecisionConstants.ADVANCE_TOO_HIGH_MSG)
        @get:Digits(integer = PricePrecisionConstants.PRICE_INTEGER_PART, fraction = PricePrecisionConstants.PRICE_DECIMAL_PART, message = PricePrecisionConstants.ADVANCE_TOO_PRECISE_MSG)
        var advance: BigDecimal,
        message: Message,
        price: BigDecimal
) : Offer(message, price) {
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

class AdvanceNoGreaterThanPriceValidator : ConstraintValidator<AdvanceNoGreaterThanPrice, PlainAdvanceOffer> {
    override fun isValid(value: PlainAdvanceOffer, context: ConstraintValidatorContext): Boolean {
        return value.advance <= value.price
    }
}