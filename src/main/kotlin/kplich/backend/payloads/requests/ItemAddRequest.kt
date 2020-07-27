package kplich.backend.payloads.requests

import kplich.backend.entities.Item
import kplich.backend.entities.Item.Companion.ADDING_USER_REQUIRED_MSG
import kplich.backend.entities.Item.Companion.CATEGORY_REQUIRED_MSG
import kplich.backend.entities.Item.Companion.DESCRIPTION_LENGTH_MSG
import kplich.backend.entities.Item.Companion.DESCRIPTION_MAX_LENGTH
import kplich.backend.entities.Item.Companion.DESCRIPTION_MIN_LENGTH
import kplich.backend.entities.Item.Companion.PHOTOS_REQUIRED_MSG
import kplich.backend.entities.Item.Companion.PHOTOS_SIZE_MSG
import kplich.backend.entities.Item.Companion.PRICE_REQUIRED_MSG
import kplich.backend.entities.Item.Companion.STATUS_REQUIRED_MSG
import kplich.backend.entities.Item.Companion.TITLE_LENGTH_MSG
import kplich.backend.entities.Item.Companion.TITLE_MAX_LENGTH
import kplich.backend.entities.Item.Companion.TITLE_MIN_LENGTH
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class ItemAddRequest(
        @get:NotBlank(message = Item.TITLE_REQURIED_MSG)
        @get:Size(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH, message = TITLE_LENGTH_MSG)
        val title: String,

        @get:NotBlank(message = Item.DESCRIPTION_REQUIRED_MSG)
        @get:Size(min = DESCRIPTION_MIN_LENGTH, max = DESCRIPTION_MAX_LENGTH, message = DESCRIPTION_LENGTH_MSG)
        val description: String,

        @get:NotNull(message = PRICE_REQUIRED_MSG)
        val price: Float,

        @get:NotNull(message = ADDING_USER_REQUIRED_MSG)
        val addedBy: Long,

        @get:NotNull(message = CATEGORY_REQUIRED_MSG)
        val category: Int,

        @get:NotBlank(message = STATUS_REQUIRED_MSG)
        val usedStatus: String,

        @get:NotNull(message = PHOTOS_REQUIRED_MSG)
        @get:Size(min = 0, max = 8, message = PHOTOS_SIZE_MSG)
        val photos: List<String>
) {
    val addedOn: LocalDateTime = LocalDateTime.now()
    val closedOn: LocalDateTime? = null
}
