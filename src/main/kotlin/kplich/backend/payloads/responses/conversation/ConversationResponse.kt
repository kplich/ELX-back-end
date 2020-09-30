package kplich.backend.payloads.responses.conversation

import kplich.backend.payloads.responses.authentication.SimpleUserResponse
import kplich.backend.payloads.responses.items.ItemResponse

data class ConversationResponse(
        val id: Long,
        val item: ItemResponse,
        val interestedUser: SimpleUserResponse,
        val messages: List<MessageResponse>
)
