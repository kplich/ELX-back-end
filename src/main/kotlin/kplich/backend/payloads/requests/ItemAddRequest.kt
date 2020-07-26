package kplich.backend.payloads.requests

import kplich.backend.entities.Item
import kplich.backend.entities.Item.Companion.ADDED_DATE_REQUIRED_MSG
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
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class ItemAddRequest(
        @NotBlank(message = Item.TITLE_REQURIED_MSG)
        @Size(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH, message = TITLE_LENGTH_MSG)
        val title: String,

        @NotBlank(message = Item.DESCRIPTION_REQUIRED_MSG)
        @Size(min = DESCRIPTION_MIN_LENGTH, max = DESCRIPTION_MAX_LENGTH, message = DESCRIPTION_LENGTH_MSG)
        val description: String,

        @NotNull(message = PRICE_REQUIRED_MSG)
        val price: Float,

        @NotBlank(message = ADDED_DATE_REQUIRED_MSG)
        val addedBy: String,

        @NotBlank(message = CATEGORY_REQUIRED_MSG)
        val category: String,

        @NotBlank(message = STATUS_REQUIRED_MSG)
        val usedStatus: String,

        @NotNull(message = PHOTOS_REQUIRED_MSG)
        @Size(min = 0, max = 8, message = PHOTOS_SIZE_MSG)
        val photoUrls: List<String>
)
