package kplich.backend.user.payloads.responses.items

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import kplich.backend.authentication.payloads.responses.SimpleUserResponse
import kplich.backend.conversation.payloads.responses.offer.OfferResponse
import kplich.backend.items.entities.UsedStatus
import kplich.backend.items.payloads.responses.CategoryResponse
import java.math.BigDecimal
import java.time.LocalDateTime

@ApiModel(description = "response with data about an item already sold")
data class ItemSoldResponse(
        override val id: Long,
        override val title: String,
        override val description: String,
        override val price: BigDecimal,
        override val addedBy: SimpleUserResponse,
        override val addedOn: LocalDateTime,
        override val category: CategoryResponse,
        override val usedStatus: UsedStatus,
        override val photoUrl: String,
        @ApiModelProperty("offer on which both parties agreed")
        val offer: OfferResponse
) : AbstractUserItem(id, title, description, price, addedBy, addedOn, category, usedStatus, photoUrl)
