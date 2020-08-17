package kplich.backend.payloads.requests.items

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
import kplich.backend.entities.OfferType
import java.math.BigDecimal
import javax.persistence.EnumType
import javax.persistence.Enumerated
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
data class NewOfferRequest(
        @Enumerated(EnumType.STRING)
        val type: OfferType,

        @get:NotNull(message = PRICE_REQUIRED_MSG)
        @get:DecimalMin(
                value = PRICE_MINIMUM_STRING,
                inclusive = true,
                message = PRICE_TOO_LOW_MSG)
        @get:DecimalMax(
                value = PRICE_MAXIMUM_STRING,
                inclusive = true,
                message = PRICE_TOO_HIGH_MSG)
        @get:Digits(
                integer = PRICE_INTEGER_PART,
                fraction = PRICE_DECIMAL_PART,
                message = PRICE_TOO_PRECISE_MSG)
        val price: BigDecimal,

        @get:NotNull(message = ADVANCE_REQUIRED_MSG)
        @get:DecimalMin(
                value = PRICE_MINIMUM_STRING,
                inclusive = true,
                message = ADVANCE_TOO_LOW_MSG)
        @get:DecimalMax(
                value = PRICE_MAXIMUM_STRING,
                inclusive = true,
                message = ADVANCE_TOO_HIGH_MSG)
        @get:Digits(
                integer = PRICE_INTEGER_PART,
                fraction = PRICE_DECIMAL_PART,
                message = ADVANCE_TOO_PRECISE_MSG)
        val advance: BigDecimal
)

@Suppress("unused") // parameters required by constraint validation?
@Constraint(validatedBy = [AdvanceNoGreaterThanPriceValidator::class])
@Target(AnnotationTarget.CLASS)
annotation class AdvanceNoGreaterThanPrice(
        val message: String = "",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

class AdvanceNoGreaterThanPriceValidator
    : ConstraintValidator<AdvanceNoGreaterThanPrice, NewOfferRequest> {
    override fun isValid(value: NewOfferRequest, context: ConstraintValidatorContext): Boolean {
        return value.advance <= value.price
    }
}
