package kplich.backend.conversation.payloads.responses.message

import kplich.backend.authentication.payloads.responses.SimpleUserResponse
import java.time.LocalDateTime

data class SimpleMessageResponse(
        val id: Long,
        val sendingUser: SimpleUserResponse,
        val sentOn: LocalDateTime,
        val textContent: String?,
)