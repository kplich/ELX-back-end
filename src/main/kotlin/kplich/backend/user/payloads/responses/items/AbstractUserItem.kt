package kplich.backend.user.payloads.responses.items

import kplich.backend.authentication.payloads.responses.SimpleUserResponse
import kplich.backend.items.entities.UsedStatus
import kplich.backend.items.payloads.responses.CategoryResponse
import java.math.BigDecimal
import java.time.LocalDateTime

abstract class AbstractUserItem(
        open val id: Long,
        open val title: String,
        open val description: String,
        open val price: BigDecimal,
        open val addedBy: SimpleUserResponse,
        open val addedOn: LocalDateTime,
        open val category: CategoryResponse,
        open val usedStatus: UsedStatus,
        open val photoUrl: String
) {
}
