package kplich.backend.data

import kplich.backend.authentication.payloads.responses.SimpleUserResponse
import kplich.backend.conversation.entities.offer.OfferStatus
import kplich.backend.conversation.payloads.responses.conversation.SimpleConversationResponse
import kplich.backend.conversation.payloads.responses.message.SimpleMessageResponse
import kplich.backend.conversation.payloads.responses.offer.DoubleAdvanceOfferResponse
import kplich.backend.conversation.payloads.responses.offer.PlainAdvanceOfferResponse
import kplich.backend.items.entities.UsedStatus
import kplich.backend.items.payloads.responses.CategoryResponse
import kplich.backend.user.payloads.responses.items.ItemWantedToSellResponse
import java.math.BigDecimal
import java.time.LocalDateTime

open class UserItemsData {
    val categories = mapOf(
                1 to CategoryResponse(1, "House and Garden"),
                2 to CategoryResponse(2, "Electronics"),
                3 to CategoryResponse(3, "Fashion"),
                4 to CategoryResponse(4, "Pets and Animals"),
                5 to CategoryResponse(5, "Children"),
                6 to CategoryResponse(6, "Sports and Outdoors"),
                7 to CategoryResponse(7, "Music and Film"),
                8 to CategoryResponse(8, "Education")
        )

    val users = mapOf(
                1 to SimpleUserResponse(1, "0xaa996165f901FEd36247D6606FC44D9e588e5bCB", "jasmine03"),
                2 to SimpleUserResponse(2, "0x00ae8B2b0e60444b22cdDbF1C355562f1E227e45", "maryjann"),
                3 to SimpleUserResponse(3, "0x1fD25f0C2d74c39b509174a354506cbb33B5A368", "jerrybumbleberry"),
                4 to SimpleUserResponse(4, "0x50f6Ea4198a24358A10CCcb16D8E58f91769bC87", "NewKidOnTheBlock")
        )

    val itemsWantedToSell: Map<Int, ItemWantedToSellResponse> = mapOf(
            1 to ItemWantedToSellResponse(
                    id = 1,
                    title = "Portable air conditioner, brand new",
                    description = "only used twice, perfect condition (no pun intended!)",
                    price = BigDecimal("0.4760"),
                    addedBy = users[4] ?: error("user has to exist!"),
                    addedOn = LocalDateTime.parse("2020-11-29T03:48:31.425240"),
                    category = categories[1] ?: error("category must exist"),
                    usedStatus = UsedStatus.NEW,
                    photoUrl = "https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1606618107364?alt=media&token=5b3fedb8-c292-4040-8027-b3a5410b08b7",
                    conversations = listOf(
                            SimpleConversationResponse(
                                    id = 5,
                                    interestedUser = users[1] ?: error("user must exist"),
                                    lastMessage = SimpleMessageResponse(
                                            id = 10,
                                            sentOn = LocalDateTime.parse("2020-11-29T04:30:16.024157"),
                                            sendingUser = users[1] ?: error("user must exist"),
                                            textContent = "looks fantastic, I want it right now!"
                                    ),
                                    lastOffer = DoubleAdvanceOfferResponse(
                                            id = 5,
                                            price = BigDecimal("0.5"),
                                            offerStatus = OfferStatus.AWAITING
                                    )
                            ),
                            SimpleConversationResponse(
                                    id = 4,
                                    interestedUser = users[3] ?: error("user must exist"),
                                    lastMessage = SimpleMessageResponse(
                                            id = 13,
                                            sentOn = LocalDateTime.parse("2020-11-29T04:35:53.313728"),
                                            sendingUser = users[4] ?: error("user must exist"),
                                            textContent = "nevermind..."
                                    ),
                                    lastOffer = PlainAdvanceOfferResponse(
                                            id = 7,
                                            price = BigDecimal("0.46"),
                                            advance = BigDecimal("0.36"),
                                            offerStatus = OfferStatus.CANCELLED
                                    )
                            )
                    )
            )
    )

}