package kplich.backend.payloads.requests.items

import kplich.backend.entities.Item.Companion.CATEGORY_REQUIRED_MSG
import kplich.backend.entities.Item.Companion.DESCRIPTION_LENGTH_MSG
import kplich.backend.entities.Item.Companion.DESCRIPTION_MAX_LENGTH
import kplich.backend.entities.Item.Companion.DESCRIPTION_MIN_LENGTH
import kplich.backend.entities.Item.Companion.DESCRIPTION_REQUIRED_MSG
import kplich.backend.entities.Item.Companion.PHOTOS_REQUIRED_MSG
import kplich.backend.entities.Item.Companion.PHOTOS_SIZE_MSG
import kplich.backend.entities.Item.Companion.PRICE_REQUIRED_MSG
import kplich.backend.entities.Item.Companion.PRICE_TOO_HIGH_MSG
import kplich.backend.entities.Item.Companion.PRICE_TOO_LOW_MSG
import kplich.backend.entities.Item.Companion.PRICE_TOO_PRECISE_MSG
import kplich.backend.entities.Item.Companion.STATUS_REQUIRED_MSG
import kplich.backend.entities.Item.Companion.TITLE_LENGTH_MSG
import kplich.backend.entities.Item.Companion.TITLE_MAX_LENGTH
import kplich.backend.entities.Item.Companion.TITLE_MIN_LENGTH
import kplich.backend.entities.Item.Companion.TITLE_REQURIED_MSG
import kplich.backend.entities.UsedStatus
import java.math.BigDecimal
import javax.validation.constraints.*

data class ItemUpdateRequest(
        @get:NotNull(message = ID_REQUIRED_MSG)
        @get:Min(1)
        val id: Long,

        @get:NotBlank(message = TITLE_REQURIED_MSG)
        @get:Size(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH, message = TITLE_LENGTH_MSG)
        val title: String,

        @get:NotBlank(message = DESCRIPTION_REQUIRED_MSG)
        @get:Size(min = DESCRIPTION_MIN_LENGTH, max = DESCRIPTION_MAX_LENGTH, message = DESCRIPTION_LENGTH_MSG)
        val description: String,

        @get:NotNull(message = PRICE_REQUIRED_MSG)
        @get:DecimalMin(value = "0.0", inclusive = true, message = PRICE_TOO_LOW_MSG)
        @get:DecimalMax(value = "100000000.0", inclusive = true, message = PRICE_TOO_HIGH_MSG)
        @get:Digits(integer = 9, fraction = 4, message = PRICE_TOO_PRECISE_MSG)
        val price: BigDecimal,

        @get:NotNull(message = CATEGORY_REQUIRED_MSG)
        val category: Int,

        @get:NotNull(message = STATUS_REQUIRED_MSG)
        val usedStatus: UsedStatus,

        @get:NotNull(message = PHOTOS_REQUIRED_MSG)
        @get:Size(min = 0, max = 8, message = PHOTOS_SIZE_MSG)
        val photos: List<String>
) {

    companion object {
        const val ID_REQUIRED_MSG = "Id is required to update the item"
    }
}