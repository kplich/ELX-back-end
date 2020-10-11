package kplich.backend.user.services

import kplich.backend.authentication.services.toSimpleResponse
import kplich.backend.conversation.entities.Conversation
import kplich.backend.conversation.entities.Message
import kplich.backend.conversation.payloads.responses.conversation.SimpleConversationResponse
import kplich.backend.conversation.payloads.responses.message.SimpleMessageResponse
import kplich.backend.conversation.services.toResponse

fun Conversation.toSimpleResponse(): SimpleConversationResponse {
    val lastMessage = this.messages.maxByOrNull { it.sentOn }!!
    val lastOffer = this.messages
            .filter { it.offer != null }
            .maxByOrNull { it.sentOn }
            ?.offer

    return SimpleConversationResponse(
            id = id,
            interestedUser = interestedUser.toSimpleResponse(),
            lastMessage = lastMessage.toSimpleResponse(),
            lastOffer = lastOffer?.toResponse()
    )
}

fun Message.toSimpleResponse(): SimpleMessageResponse {
    return SimpleMessageResponse(
            id = id,
            sendingUser = sender.toSimpleResponse(),
            sentOn = sentOn,
            textContent = content
    )
}