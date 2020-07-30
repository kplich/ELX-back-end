package kplich.backend.payloads.responses.items

import kplich.backend.entities.UsedStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class ItemResponse(
        val id: Long,
        val title: String,
        val description: String,
        val price: BigDecimal,
        val addedBy: ItemAddedByResponse,
        val addedOn: LocalDateTime,
        val category: ItemCategoryResponse,
        val usedStatus: UsedStatus,
        val photoUrls: List<String>,
        val closedOn: LocalDateTime?
) {
    data class ItemAddedByResponse(
            val id: Long,
            val username: String
    )

    data class ItemCategoryResponse(
            val id: Int,
            val name: String
    )
}
