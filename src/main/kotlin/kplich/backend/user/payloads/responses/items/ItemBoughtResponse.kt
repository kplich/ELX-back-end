package kplich.backend.user.payloads.responses.items

import kplich.backend.items.entities.UsedStatus
import kplich.backend.conversation.payloads.responses.offer.OfferResponse
import kplich.backend.items.payloads.responses.CategoryResponse
import kplich.backend.authentication.payloads.responses.SimpleUserResponse
import java.math.BigDecimal
import java.time.LocalDateTime

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
        val offer: OfferResponse
) : AbstractUserItem(id, title, description, price, addedBy, addedOn, category, usedStatus, photoUrl)