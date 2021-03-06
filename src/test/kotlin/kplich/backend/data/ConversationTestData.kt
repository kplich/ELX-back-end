package kplich.backend.data

import kplich.backend.conversation.entities.offer.OfferStatus
import kplich.backend.items.entities.UsedStatus
import kplich.backend.conversation.payloads.responses.conversation.FullConversationResponse
import kplich.backend.conversation.payloads.responses.message.MessageResponse
import kplich.backend.conversation.payloads.responses.offer.OfferResponse
import kplich.backend.conversation.payloads.responses.offer.PlainAdvanceOfferResponse
import kplich.backend.items.payloads.responses.ItemResponse
import java.math.BigDecimal
import java.time.LocalDateTime

abstract class ConversationTestData : BaseItemConversationTestData() {

    protected val items
        get(): Map<Long, ItemResponse> = mapOf(
                4L to ItemResponse(
                        id = 4L,
                        description = "First item for testing conversations, item 4",
                        title = "Testing conversations, item 4",
                        price = BigDecimal("1.0100"),
                        addedBy = users[1] ?: error("user not found"),
                        addedOn = LocalDateTime.parse("2020-05-05T21:40:10.096853"),
                        category = categories[1] ?: error("category not found"),
                        usedStatus = UsedStatus.NEW,
                        photoUrls = emptyList(),
                        closedOn = null
                ),
                5L to ItemResponse(
                        id = 5L,
                        description = "Second item for testing conversations, item 5",
                        title = "Testing conversations, item 5",
                        price = BigDecimal("1.0100"),
                        addedBy = users[1] ?: error("user not found"),
                        addedOn = LocalDateTime.parse("2020-05-05T21:40:10.096853"),
                        category = categories[1] ?: error("category not found"),
                        usedStatus = UsedStatus.NEW,
                        photoUrls = emptyList(),
                        closedOn = null
                ),
                6L to ItemResponse(
                        id = 6L,
                        description = "Third item for testing conversations, item 6",
                        title = "Testing conversations, item 6",
                        price = BigDecimal("1.0100"),
                        addedBy = users[1] ?: error("user not found"),
                        addedOn = LocalDateTime.parse("2020-05-05T21:40:10.096853"),
                        category = categories[1] ?: error("category not found"),
                        usedStatus = UsedStatus.NEW,
                        photoUrls = emptyList(),
                        closedOn = null
                ),
                7L to ItemResponse(
                        id = 7L,
                        description = "Fourth item for testing conversations, item 7",
                        title = "Testing conversations, item 7",
                        price = BigDecimal("1.0100"),
                        addedBy = users[1] ?: error("user not found"),
                        addedOn = LocalDateTime.parse("2020-05-05T21:40:10.096853"),
                        category = categories[1] ?: error("category not found"),
                        usedStatus = UsedStatus.NEW,
                        photoUrls = emptyList(),
                        closedOn = null
                )
        )

    protected val offers
        get(): Map<Long, OfferResponse> = mapOf(
                1L to PlainAdvanceOfferResponse(
                        id = 1L,
                        advance = BigDecimal("0.5000"),
                        price = BigDecimal("1.0000"),
                        offerStatus = OfferStatus.AWAITING
                ),
                2L to PlainAdvanceOfferResponse(
                        id = 2L,
                        advance = BigDecimal("0.6700"),
                        price = BigDecimal("1.0000"),
                        offerStatus = OfferStatus.AWAITING
                ),
                3L to PlainAdvanceOfferResponse(
                        id = 3L,
                        advance = BigDecimal("1.0000"),
                        price = BigDecimal("1.5000"),
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
        get(): Map<Long, FullConversationResponse> = mapOf(
                1L to FullConversationResponse(
                        id = 1L,
                        item = items[4] ?: error("item not found"),
                        interestedUser = users[2] ?: error("item not found"),
                        messages = listOf(1L, 2L, 3L).map {
                            messages[it] ?: error("message not found")
                        }
                ),
                2L to FullConversationResponse(
                        id = 2L,
                        item = items[6] ?: error("item not found"),
                        interestedUser = users[2] ?: error("item not found"),
                        messages = listOf(4L, 5L, 6L).map {
                            messages[it] ?: error("message not found")
                        }
                ),
                3L to FullConversationResponse(
                        id = 3L,
                        item = items[7] ?: error("item not found"),
                        interestedUser = users[2] ?: error("item not found"),
                        messages = listOf(7L).map {
                            messages[it] ?: error("message not found")
                        }
                ),
                4L to FullConversationResponse(
                        id = 4L,
                        item = items[7] ?: error("item not found"),
                        interestedUser = users[3] ?: error("item not found"),
                        messages = listOf(8L).map {
                            messages[it] ?: error("message not found")
                        }
                ),
        )
}
