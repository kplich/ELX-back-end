package kplich.backend.payloads.responses.conversation.conversation

import kplich.backend.payloads.responses.conversation.message.MessageResponse
import kplich.backend.payloads.responses.user.SimpleUserResponse
import kplich.backend.payloads.responses.items.item.ItemResponse

data class ConversationResponse(
        val id: Long,
        val item: ItemResponse,
        val interestedUser: SimpleUserResponse,
        val messages: List<MessageResponse>
)
