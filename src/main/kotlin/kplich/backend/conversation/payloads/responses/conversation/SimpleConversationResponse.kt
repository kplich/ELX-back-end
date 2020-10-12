package kplich.backend.conversation.payloads.responses.conversation

import kplich.backend.conversation.payloads.responses.message.SimpleMessageResponse
import kplich.backend.conversation.payloads.responses.offer.OfferResponse
import kplich.backend.authentication.payloads.responses.SimpleUserResponse

data class SimpleConversationResponse(
        val id: Long,
        val interestedUser: SimpleUserResponse,
        val lastMessage: SimpleMessageResponse,
        val lastOffer: OfferResponse?
)