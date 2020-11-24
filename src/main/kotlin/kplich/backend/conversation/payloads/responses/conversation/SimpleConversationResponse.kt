package kplich.backend.conversation.payloads.responses.conversation

import io.swagger.annotations.ApiModel
import kplich.backend.conversation.payloads.responses.message.SimpleMessageResponse
import kplich.backend.conversation.payloads.responses.offer.OfferResponse
import kplich.backend.authentication.payloads.responses.SimpleUserResponse

@ApiModel(description = "response containing only the last message and the last offer in the conversation")
data class SimpleConversationResponse(
        val id: Long,
        val interestedUser: SimpleUserResponse,
        val lastMessage: SimpleMessageResponse,
        val lastOffer: OfferResponse?
)