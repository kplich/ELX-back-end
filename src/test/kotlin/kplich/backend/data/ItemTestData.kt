package kplich.backend.data

import kplich.backend.items.entities.UsedStatus
import kplich.backend.items.payloads.responses.ItemResponse
import java.math.BigDecimal
import java.time.LocalDateTime

abstract class ItemTestData : BaseItemConversationTestData() {
    protected val items
        get(): Map<Long, ItemResponse> = mapOf(
                1L to ItemResponse(
                        id = 1L,
                        title = "1 Quick title that will have more than 10 characters",
                        description = "1 Quick description of an open item in category House and Garden",
                        price = BigDecimal("1.2345"),
                        addedBy = users[1] ?: error("user not found"),
                        addedOn = LocalDateTime.parse("2020-05-05T21:40:10.096853"),
                        category = categories[1] ?: error("category not found"),
                        usedStatus = UsedStatus.NEW,
                        photoUrls = emptyList(),
                        closedOn = null
                ),
                2L to ItemResponse(
                        id = 2L,
                        title = "2 Quick title that will have more than 10 characters",
                        description = "2 Quick description of a closed item! in category Electronics",
                        price = BigDecimal("2.0000"),
                        addedBy = users[2] ?: error("user not found"),
                        addedOn = LocalDateTime.parse("2020-06-06T21:40:10.096853"),
                        category = categories[2] ?: error("category not found"),
                        usedStatus = UsedStatus.USED,
                        photoUrls = emptyList(),
                        closedOn = LocalDateTime.parse("2020-06-07T21:37:00.420069")
                ),
                3L to ItemResponse(
                        id = 3L,
                        title = "3 Quick title that will have more than 10 characters",
                        description = "3 Quick description of an in category Fashion",
                        price = BigDecimal("3.3333"),
                        addedBy = users[3] ?: error("user not found"),
                        addedOn = LocalDateTime.parse("2020-07-07T21:40:10.096853"),
                        category = categories[3] ?: error("category not found"),
                        usedStatus = UsedStatus.NOT_APPLICABLE,
                        photoUrls = emptyList(),
                        closedOn = null
                )
        )
}
