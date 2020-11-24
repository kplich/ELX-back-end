package kplich.backend.user.payloads.responses.items

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import kplich.backend.items.entities.UsedStatus
import kplich.backend.conversation.payloads.responses.offer.OfferResponse
import kplich.backend.items.payloads.responses.CategoryResponse
import kplich.backend.authentication.payloads.responses.SimpleUserResponse
import java.math.BigDecimal
import java.time.LocalDateTime

@ApiModel(description = "response with data about an item already bought")
class ItemBoughtResponse(
        id: Long,
        title: String,
        description: String,
        price: BigDecimal,
        addedBy: SimpleUserResponse,
        addedOn: LocalDateTime,
        category: CategoryResponse,
        usedStatus: UsedStatus,
        photoUrl: String,
        @ApiModelProperty("offer on which both parties agreed")
        val offer: OfferResponse
) : AbstractUserItem(id, title, description, price, addedBy, addedOn, category, usedStatus, photoUrl)