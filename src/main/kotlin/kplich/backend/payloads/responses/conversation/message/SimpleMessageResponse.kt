package kplich.backend.payloads.responses.conversation.message

import kplich.backend.payloads.responses.user.SimpleUserResponse
import java.time.LocalDateTime

data class SimpleMessageResponse(
        val id: Long,
        val sendingUser: SimpleUserResponse,
        val sentOn: LocalDateTime,
        val textContent: String?,
)