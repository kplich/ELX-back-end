package kplich.backend.conversation.payloads.responses.conversation

import io.swagger.annotations.ApiModel
import kplich.backend.conversation.payloads.responses.message.MessageResponse
import kplich.backend.authentication.payloads.responses.SimpleUserResponse
import kplich.backend.items.payloads.responses.ItemResponse

@ApiModel(description = "response containing all messages in the conversation")
data class FullConversationResponse(
        val id: Long,
        val item: ItemResponse,
        val interestedUser: SimpleUserResponse,
        val messages: List<MessageResponse>
)
