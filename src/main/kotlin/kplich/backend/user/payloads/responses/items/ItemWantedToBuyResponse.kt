package kplich.backend.user.payloads.responses.items

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import kplich.backend.items.entities.UsedStatus
import kplich.backend.conversation.payloads.responses.conversation.SimpleConversationResponse
import kplich.backend.items.payloads.responses.CategoryResponse
import kplich.backend.authentication.payloads.responses.SimpleUserResponse
import java.math.BigDecimal
import java.time.LocalDateTime

@ApiModel(description = "response with data about an item that the user wants to buy")
class ItemWantedToBuyResponse(
        id: Long,
        title: String,
        description: String,
        price: BigDecimal,
        addedBy: SimpleUserResponse,
        addedOn: LocalDateTime,
        category: CategoryResponse,
        usedStatus: UsedStatus,
        photoUrl: String,
        @ApiModelProperty("preview of the conversation between users")
        val conversation: SimpleConversationResponse
): AbstractUserItem(id, title, description, price, addedBy, addedOn, category, usedStatus, photoUrl)