package kplich.backend.data

import kplich.backend.authentication.payloads.responses.SimpleUserResponse
import kplich.backend.conversation.entities.offer.OfferStatus
import kplich.backend.conversation.payloads.responses.conversation.SimpleConversationResponse
import kplich.backend.conversation.payloads.responses.message.SimpleMessageResponse
import kplich.backend.conversation.payloads.responses.offer.DoubleAdvanceOfferResponse
import kplich.backend.conversation.payloads.responses.offer.PlainAdvanceOfferResponse
import kplich.backend.items.entities.UsedStatus
import kplich.backend.items.payloads.responses.CategoryResponse
import kplich.backend.user.payloads.responses.items.ItemBoughtResponse
import kplich.backend.user.payloads.responses.items.ItemSoldResponse
import kplich.backend.user.payloads.responses.items.ItemWantedToBuyResponse
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
                                            price = BigDecimal("0.5000"),
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
                                            price = BigDecimal("0.4600"),
                                            advance = BigDecimal("0.3600"),
                                            offerStatus = OfferStatus.CANCELLED
                                    )
                            )
                    )
            ),
            2 to ItemWantedToSellResponse(
                    id = 2,
                    title = "Dog kennel",
                    description = "120 cm x 80 cm x 100 cm\nit's possible to send the kennel by post!",
                    price = BigDecimal("0.5930"),
                    addedBy = users[4] ?: error("user must exist!"),
                    addedOn = LocalDateTime.parse("2020-11-29T03:52:16.22115"),
                    category = categories[4] ?: error("category must exist"),
                    usedStatus = UsedStatus.USED,
                    photoUrl = "https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1606618333377?alt=media&token=a0969322-1757-4381-ac7b-82c2324dfb43",
                    conversations = emptyList()

            )
    )

    val itemsSold: Map<Int, ItemSoldResponse> = mapOf(
            7 to ItemSoldResponse(
                    id = 7,
                    title = "New Balance Shoes, Size 39",
                    description = "a bit worn out, but still in a good condition. very hard to find nowadays!",
                    price = BigDecimal("0.1220"),
                    addedBy = users[4] ?: error("user must exist"),
                    addedOn = LocalDateTime.parse("2020-11-29T04:22:17.870117"),
                    category = categories[3] ?: error("category must exist"),
                    usedStatus = UsedStatus.USED,
                    photoUrl = "https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1606620134985?alt=media&token=482cef7a-2b39-47ec-9643-eae2cec859f8",
                    offer = PlainAdvanceOfferResponse(
                            id = 4,
                            advance = BigDecimal("0.0600"),
                            price = BigDecimal("0.1200"),
                            offerStatus = OfferStatus.ACCEPTED,
                            contractAddress = "0xB1Bd1AffDd5eD74F8d09Ce5e9fC51d064C8f8497"
                    )
            )
    )

    val itemsWantedToBuy: Map<Int, ItemWantedToBuyResponse> = mapOf(
            6 to ItemWantedToBuyResponse(
                    id = 6,
                    title = "Cute socks with bears",
                    description = "Very comfortable socks with bears\nSmall size 36-38\nNEW!",
                    price = BigDecimal("0.0052"),
                    addedBy = users[2] ?: error("user must exist"),
                    addedOn = LocalDateTime.parse("2020-11-15T18:25:48.767058"),
                    category = categories[3] ?: error("category must exist"),
                    usedStatus = UsedStatus.NEW,
                    photoUrl = "https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1605461144016?alt=media&token=11caee65-a9b2-426f-baca-853fddb52147",
                    conversation = SimpleConversationResponse(
                            id = 1,
                            interestedUser = users[4] ?: error("user must exist"),
                            lastMessage = SimpleMessageResponse(
                                    id = 4,
                                    sendingUser = users[2] ?: error("user must exist"),
                                    sentOn = LocalDateTime.parse("2020-11-29T04:15:59.528206"),
                                    textContent = "no, unfortunately not :("
                            ),
                            lastOffer = null
                    )
            )
    )

    val itemsBought: Map<Int, ItemBoughtResponse> = mapOf(
            5 to ItemBoughtResponse(
                    id = 5,
                    title = "Aquarium set (720 litres)",
                    description = "720 litres (200x60x60)/alu diversa cover, module lighting, metal cupboard\navailable after fish sold or together with fish\n",
                    price = BigDecimal("1.4780"),
                    addedBy = users[2] ?: error("user must exist"),
                    addedOn = LocalDateTime.parse("2020-11-15T18:22:00.21909"),
                    category = categories[4] ?: error("category must exist"),
                    usedStatus = UsedStatus.USED,
                    photoUrl = "https://firebasestorage.googleapis.com/v0/b/elx-front-end.appspot.com/o/items%2F1605460912648?alt=media&token=39053190-ca35-479e-928b-2b86b8ec4fd8",
                    offer = DoubleAdvanceOfferResponse(
                            id = 2,
                            price = BigDecimal("1.5430"),
                            offerStatus = OfferStatus.ACCEPTED,
                            contractAddress = "0x6FDecbFb659b3680B5ab576B0d70c495d6374269"
                    )
            )
    )
}
