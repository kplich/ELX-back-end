package kplich.backend.user.payloads.responses.items

import kplich.backend.items.entities.UsedStatus
import kplich.backend.items.payloads.responses.CategoryResponse
import kplich.backend.authentication.payloads.responses.SimpleUserResponse
import java.math.BigDecimal
import java.time.LocalDateTime

abstract class AbstractUserItem(
        val id: Long,
        val title: String,
        val description: String,
        val price: BigDecimal,
        val addedBy: SimpleUserResponse,
        val addedOn: LocalDateTime,
        val category: CategoryResponse,
        val usedStatus: UsedStatus,
        val photoUrl: String
)