package kplich.backend.payloads.responses.items

import kplich.backend.entities.UsedStatus
import kplich.backend.payloads.responses.authentication.SimpleUserResponse
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
