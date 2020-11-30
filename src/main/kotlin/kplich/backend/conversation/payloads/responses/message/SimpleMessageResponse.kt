package kplich.backend.conversation.payloads.responses.message

import io.swagger.annotations.ApiModel
import kplich.backend.authentication.payloads.responses.SimpleUserResponse
import java.time.LocalDateTime

@ApiModel(description = "simplified response with only basic information about an offer")
data class SimpleMessageResponse(
        val id: Long,
        val sendingUser: SimpleUserResponse,
        val sentOn: LocalDateTime,
        val textContent: String?,
)