package kplich.backend.items.payloads.requests

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import kplich.backend.configurations.PricePrecisionConstants.PRICE_DECIMAL_PART
import kplich.backend.configurations.PricePrecisionConstants.PRICE_INTEGER_PART
import kplich.backend.configurations.PricePrecisionConstants.PRICE_MAXIMUM_STRING
import kplich.backend.configurations.PricePrecisionConstants.PRICE_MINIMUM_STRING
import kplich.backend.configurations.PricePrecisionConstants.PRICE_REQUIRED_MSG
import kplich.backend.configurations.PricePrecisionConstants.PRICE_TOO_HIGH_MSG
import kplich.backend.configurations.PricePrecisionConstants.PRICE_TOO_LOW_MSG
import kplich.backend.configurations.PricePrecisionConstants.PRICE_TOO_PRECISE_MSG
import kplich.backend.items.entities.Item
import kplich.backend.items.entities.Item.Companion.CATEGORY_REQUIRED_MSG
import kplich.backend.items.entities.Item.Companion.DESCRIPTION_LENGTH_MSG
import kplich.backend.items.entities.Item.Companion.DESCRIPTION_MAX_LENGTH
import kplich.backend.items.entities.Item.Companion.DESCRIPTION_MIN_LENGTH
import kplich.backend.items.entities.Item.Companion.PHOTOS_REQUIRED_MSG
import kplich.backend.items.entities.Item.Companion.PHOTOS_SIZE_MSG
import kplich.backend.items.entities.Item.Companion.STATUS_REQUIRED_MSG
import kplich.backend.items.entities.Item.Companion.TITLE_LENGTH_MSG
import kplich.backend.items.entities.Item.Companion.TITLE_MAX_LENGTH
import kplich.backend.items.entities.Item.Companion.TITLE_MIN_LENGTH
import kplich.backend.items.entities.UsedStatus
import java.math.BigDecimal
import javax.validation.constraints.*

@ApiModel(description = "request for adding a new item")
data class ItemAddRequest(
        @get:NotBlank(message = Item.TITLE_REQURIED_MSG)
        @get:Size(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH, message = TITLE_LENGTH_MSG)
        val title: String,

        @get:NotBlank(message = Item.DESCRIPTION_REQUIRED_MSG)
        @get:Size(min = DESCRIPTION_MIN_LENGTH, max = DESCRIPTION_MAX_LENGTH, message = DESCRIPTION_LENGTH_MSG)
        val description: String,

        @get:NotNull(message = PRICE_REQUIRED_MSG)
        @get:DecimalMin(value = PRICE_MINIMUM_STRING, inclusive = true, message = PRICE_TOO_LOW_MSG)
        @get:DecimalMax(value = PRICE_MAXIMUM_STRING, inclusive = true, message = PRICE_TOO_HIGH_MSG)
        @get:Digits(integer = PRICE_INTEGER_PART, fraction = PRICE_DECIMAL_PART, message = PRICE_TOO_PRECISE_MSG)
        val price: BigDecimal,

        @ApiModelProperty("ID of the category of the item")
        @get:NotNull(message = CATEGORY_REQUIRED_MSG)
        val category: Int,

        @ApiModelProperty("status of the item - was it used or not?")
        @get:NotNull(message = STATUS_REQUIRED_MSG)
        val usedStatus: UsedStatus,

        @ApiModelProperty("list of URLs of the photo items")
        @get:NotNull(message = PHOTOS_REQUIRED_MSG)
        @get:Size(min = 0, max = 8, message = PHOTOS_SIZE_MSG)
        val photos: List<String>,
)
