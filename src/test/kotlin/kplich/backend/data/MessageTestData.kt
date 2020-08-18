package kplich.backend.data

import kplich.backend.entities.OfferStatus
import kplich.backend.entities.OfferType
import kplich.backend.entities.UsedStatus
import kplich.backend.payloads.responses.authentication.SimpleUserResponse
import kplich.backend.payloads.responses.items.*
import java.math.BigDecimal
import java.time.LocalDateTime

abstract class MessageTestData {
    private val categories
        get(): Map<Int, CategoryResponse> = mapOf(
                1 to CategoryResponse(1, "House and Garden"),
                2 to CategoryResponse(2, "Electronics"),
                3 to CategoryResponse(3, "Fashion")
        )

    private val users
        get(): Map<Int, SimpleUserResponse> = mapOf(
                1 to SimpleUserResponse(1, "kplich"),
                2 to SimpleUserResponse(2, "kplich2"),
                3 to SimpleUserResponse(3, "kplich3")
        )

    protected val items
        get(): Map<Long, ItemResponse> = mapOf(
                4L to ItemResponse(
                        id = 4L,
                        title = "First item for testing conversations, item 4",
                        description = "Testing conversations, item 4",
                        price = BigDecimal("1.01"),
                        addedBy = users[1] ?: error("user not found"),
                        addedOn = LocalDateTime.parse("2020-05-05T21:40:10.096853"),
                        category = categories[1] ?: error("category not found"),
                        usedStatus = UsedStatus.NEW,
                        photoUrls = emptyList(),
                        closedOn = null
                ),
                5L to ItemResponse(
                        id = 5L,
                        title = "Second item for testing conversations, item 5",
                        description = "Testing conversations, item 5",
                        price = BigDecimal("1.01"),
                        addedBy = users[1] ?: error("user not found"),
                        addedOn = LocalDateTime.parse("2020-05-05T21:40:10.096853"),
                        category = categories[1] ?: error("category not found"),
                        usedStatus = UsedStatus.NEW,
                        photoUrls = emptyList(),
                        closedOn = null
                ),
                6L to ItemResponse(
                        id = 6L,
                        title = "Third item for testing conversations, item 6",
                        description = "Testing conversations, item 6",
                        price = BigDecimal("1.01"),
                        addedBy = users[1] ?: error("user not found"),
                        addedOn = LocalDateTime.parse("2020-05-05T21:40:10.096853"),
                        category = categories[1] ?: error("category not found"),
                        usedStatus = UsedStatus.NEW,
                        photoUrls = emptyList(),
                        closedOn = null
                ),
                7L to ItemResponse(
                        id = 7L,
                        title = "Fourth item for testing conversations, item 7",
                        description = "Testing conversations, item 7",
                        price = BigDecimal("1.01"),
                        addedBy = users[1] ?: error("user not found"),
                        addedOn = LocalDateTime.parse("2020-05-05T21:40:10.096853"),
                        category = categories[1] ?: error("category not found"),
                        usedStatus = UsedStatus.NEW,
                        photoUrls = emptyList(),
                        closedOn = null
                )
        )

    private val offers
        get(): Map<Long, OfferResponse> = mapOf(
                1L to OfferResponse(
                        id = 1L,
                        type = OfferType.PLAIN_ADVANCE,
                        advance = BigDecimal("0.5"),
                        price = BigDecimal("1.0"),
                        offerStatus = OfferStatus.AWAITING
                ),
                2L to OfferResponse(
                        id = 2L,
                        type = OfferType.PLAIN_ADVANCE,
                        advance = BigDecimal("0.67"),
                        price = BigDecimal("1.0"),
                        offerStatus = OfferStatus.AWAITING
                ),
                3L to OfferResponse(
                        id = 3L,
                        type = OfferType.PLAIN_ADVANCE,
                        advance = BigDecimal("1.0"),
                        price = BigDecimal("1.5"),
                        offerStatus = OfferStatus.AWAITING
                ),
        )

    private val messages
        get(): Map<Long, MessageResponse> = mapOf(
                1L to MessageResponse(
                        id = 1L,
                        sendingUser = users[2] ?: error("user not found"),
                        sentOn = LocalDateTime.parse("2020-05-05T23:40:10.096853"),
                        textContent = "First message"
                ),
                2L to MessageResponse(
                        id = 2L,
                        sendingUser = users[1] ?: error("user not found"),
                        sentOn = LocalDateTime.parse("2020-05-05T23:45:10.096853"),
                        textContent = "First reply"
                ),
                3L to MessageResponse(
                        id = 3L,
                        sendingUser = users[2] ?: error("user not found"),
                        sentOn = LocalDateTime.parse("2020-05-05T23:50:10.096853"),
                        textContent = "Another message"
                ),
                4L to MessageResponse(
                        id = 4L,
                        sendingUser = users[2] ?: error("user not found"),
                        sentOn = LocalDateTime.parse("2020-05-05T23:40:10.096853"),
                        textContent = "Can I send an offer?"
                ),
                5L to MessageResponse(
                        id = 5L,
                        sendingUser = users[1] ?: error("user not found"),
                        sentOn = LocalDateTime.parse("2020-05-05T23:45:10.096853"),
                        textContent = "Sure thing!"
                ),
                6L to MessageResponse(
                        id = 6L,
                        sendingUser = users[2] ?: error("user not found"),
                        sentOn = LocalDateTime.parse("2020-05-05T23:40:10.096853"),
                        textContent = "There I go!",
                        offer = offers[1] ?: error("offer not found")
                ),
                7L to MessageResponse(
                        id = 7L,
                        sendingUser = users[2] ?: error("user not found"),
                        sentOn = LocalDateTime.parse("2020-05-05T23:40:10.096853"),
                        textContent = "My offer",
                        offer = offers[2] ?: error("offer not found")
                ),
                8L to MessageResponse(
                        id = 8L,
                        sendingUser = users[3] ?: error("user not found"),
                        sentOn = LocalDateTime.parse("2020-05-05T23:45:10.096853"),
                        textContent = "I'm sure mine's better :P",
                        offer = offers[3] ?: error("offer not found")
                ),
        )

    protected val conversations
        get(): Map<Long, ConversationResponse> = mapOf(
                1L to ConversationResponse(
                        id = 1L,
                        item = items[4] ?: error("item not found"),
                        interestedUser = users[2] ?: error("item not found"),
                        messages = listOf(1L, 2L, 3L).map {
                            messages[it] ?: error("message not found")
                        }
                ),
                2L to ConversationResponse(
                        id = 2L,
                        item = items[6] ?: error("item not found"),
                        interestedUser = users[2] ?: error("item not found"),
                        messages = listOf(4L, 5L, 6L).map {
                            messages[it] ?: error("message not found")
                        }
                ),
                3L to ConversationResponse(
                        id = 3L,
                        item = items[7] ?: error("item not found"),
                        interestedUser = users[2] ?: error("item not found"),
                        messages = listOf(7L).map {
                            messages[it] ?: error("message not found")
                        }
                ),
                4L to ConversationResponse(
                        id = 4L,
                        item = items[7] ?: error("item not found"),
                        interestedUser = users[3] ?: error("item not found"),
                        messages = listOf(8L).map {
                            messages[it] ?: error("message not found")
                        }
                ),
        )
}
