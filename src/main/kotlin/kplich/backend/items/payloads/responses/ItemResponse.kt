package kplich.backend.items.payloads.responses

import kplich.backend.items.entities.UsedStatus
import kplich.backend.authentication.payloads.responses.SimpleUserResponse
import java.math.BigDecimal
import java.time.LocalDateTime

data class ItemResponse(
        val id: Long,
        val title: String,
        val description: String,
        val price: BigDecimal,
        val addedBy: SimpleUserResponse,
        val addedOn: LocalDateTime,
        val category: CategoryResponse,
        val usedStatus: UsedStatus,
        val photoUrls: List<String>,
        val closedOn: LocalDateTime?
)
