package kplich.backend.payloads.responses.items

import kplich.backend.payloads.responses.authentication.SimpleUserResponse

data class ConversationResponse(
        val id: Long,
        val item: ItemResponse,
        val interestedUser: SimpleUserResponse,
        val messages: List<MessageResponse>
)
