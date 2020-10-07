package kplich.backend.payloads.responses.conversation.conversation

import kplich.backend.payloads.responses.conversation.message.SimpleMessageResponse
import kplich.backend.payloads.responses.conversation.offer.OfferResponse
import kplich.backend.payloads.responses.user.SimpleUserResponse

data class SimpleConversationResponse(
        val id: Long,
        val interestedUser: SimpleUserResponse,
        val lastMessage: SimpleMessageResponse,
        val lastOffer: OfferResponse?
)