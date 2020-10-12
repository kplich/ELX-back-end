package kplich.backend.conversation.services

import kplich.backend.conversation.entities.Conversation
import kplich.backend.conversation.entities.Message
import kplich.backend.conversation.entities.offer.DoubleAdvanceOffer
import kplich.backend.conversation.entities.offer.Offer
import kplich.backend.conversation.entities.offer.PlainAdvanceOffer
import kplich.backend.conversation.payloads.responses.conversation.FullConversationResponse
import kplich.backend.conversation.payloads.responses.message.MessageResponse
import kplich.backend.conversation.payloads.responses.offer.DoubleAdvanceOfferResponse
import kplich.backend.conversation.payloads.responses.offer.OfferResponse
import kplich.backend.conversation.payloads.responses.offer.PlainAdvanceOfferResponse
import kplich.backend.items.services.toResponse
import kplich.backend.authentication.services.toSimpleResponse

fun Conversation.toFullResponse(): FullConversationResponse {
    return FullConversationResponse(
            id = id,
            item = item.toResponse(),
            interestedUser = interestedUser.toSimpleResponse(),
            messages = messages.map { it.toResponse() }
    )
}

fun Message.toResponse(): MessageResponse {
    return MessageResponse(
            id = id,
            sendingUser = sender.toSimpleResponse(),
            sentOn = sentOn,
            textContent = content,
            offer = offer?.toResponse()
    )
}

fun Offer.toResponse(): OfferResponse {
    return when(this) {
        is PlainAdvanceOffer -> PlainAdvanceOfferResponse(
                advance = advance,
                id = id,
                price = price,
                offerStatus = offerStatus,
                contractAddress = contractAddress
        )
        is DoubleAdvanceOffer -> DoubleAdvanceOfferResponse(
                id = id,
                price = price,
                offerStatus = offerStatus,
                contractAddress = contractAddress
        )
        else -> throw IllegalArgumentException("Cannot turn into offer response")
    }
}