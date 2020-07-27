package kplich.backend.payloads.responses.items

import kplich.backend.entities.UsedStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class ItemFlatResponse(
        val title: String,
        val description: String,
        val price: BigDecimal,
        val addedBy: String,
        val added: LocalDateTime,
        val category: String,
        val usedStatus: UsedStatus,
        val photoUrls: List<String>,
        val closed: LocalDateTime?
)
