package kplich.backend.conversation.payloads.requests.offer

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import kplich.backend.configurations.PricePrecisionConstants.PRICE_DECIMAL_PART
import kplich.backend.configurations.PricePrecisionConstants.PRICE_INTEGER_PART
import kplich.backend.configurations.PricePrecisionConstants.PRICE_MAXIMUM_STRING
import kplich.backend.configurations.PricePrecisionConstants.PRICE_MINIMUM_STRING
import kplich.backend.configurations.PricePrecisionConstants.PRICE_REQUIRED_MSG
import kplich.backend.configurations.PricePrecisionConstants.PRICE_TOO_HIGH_MSG
import kplich.backend.configurations.PricePrecisionConstants.PRICE_TOO_LOW_MSG
import kplich.backend.configurations.PricePrecisionConstants.PRICE_TOO_PRECISE_MSG
import java.math.BigDecimal
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull

@JsonTypeInfo(
        use = JsonTypeInfo.Id.MINIMAL_CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "requestType"
)
@JsonSubTypes(
        JsonSubTypes.Type(value = NewPlainAdvanceOfferRequest::class, name = "NewPlainAdvanceOfferRequest"),
        JsonSubTypes.Type(value = NewDoubleAdvanceOfferRequest::class, name = "NewDoubleAdvanceOfferRequest")
)
abstract class NewOfferRequest(
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
        val price: BigDecimal
)