package kplich.backend.payloads.responses.items.item

import kplich.backend.entities.items.UsedStatus
import kplich.backend.payloads.responses.conversation.conversation.SimpleConversationResponse
import kplich.backend.payloads.responses.items.CategoryResponse
import java.math.BigDecimal
import java.time.LocalDateTime

data class ItemSoldByMeResponse(
        val id: Long,
        val title: String,
        val description: String,
        val price: BigDecimal,
        val addedOn: LocalDateTime,
        val category: CategoryResponse,
        val usedStatus: UsedStatus,
        val photoUrl: String,
        val conversations: List<SimpleConversationResponse>
)