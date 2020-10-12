package kplich.backend.conversation.payloads.requests.offer

import kplich.backend.configurations.PricePrecisionConstants
import java.math.BigDecimal
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull
import kotlin.reflect.KClass

@AdvanceNoGreaterThanPrice
class NewPlainAdvanceOfferRequest(
        price: BigDecimal,

        @get:NotNull(message = PricePrecisionConstants.ADVANCE_REQUIRED_MSG)
        @get:DecimalMin(
                value = PricePrecisionConstants.PRICE_MINIMUM_STRING,
                inclusive = true,
                message = PricePrecisionConstants.ADVANCE_TOO_LOW_MSG)
        @get:DecimalMax(
                value = PricePrecisionConstants.PRICE_MAXIMUM_STRING,
                inclusive = true,
                message = PricePrecisionConstants.ADVANCE_TOO_HIGH_MSG)
        @get:Digits(
                integer = PricePrecisionConstants.PRICE_INTEGER_PART,
                fraction = PricePrecisionConstants.PRICE_DECIMAL_PART,
                message = PricePrecisionConstants.ADVANCE_TOO_PRECISE_MSG)
        val advance: BigDecimal
) : NewOfferRequest(price)

@Suppress("unused") // parameters required by constraint validation?
@Constraint(validatedBy = [AdvanceNoGreaterThanPriceValidator::class])
@Target(AnnotationTarget.CLASS)
annotation class AdvanceNoGreaterThanPrice(
        val message: String = "",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

class AdvanceNoGreaterThanPriceValidator
    : ConstraintValidator<AdvanceNoGreaterThanPrice, NewPlainAdvanceOfferRequest> {
    override fun isValid(value: NewPlainAdvanceOfferRequest, context: ConstraintValidatorContext): Boolean {
        return value.advance <= value.price
    }
}