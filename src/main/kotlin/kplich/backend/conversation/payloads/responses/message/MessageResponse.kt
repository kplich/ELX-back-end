package kplich.backend.conversation.payloads.responses.message

import io.swagger.annotations.ApiModel
import kplich.backend.authentication.payloads.responses.SimpleUserResponse
import kplich.backend.conversation.payloads.responses.offer.OfferResponse
import java.time.LocalDateTime

@ApiModel(description = "response containing all data about a message")
data class MessageResponse(
        val id: Long,
        val sendingUser: SimpleUserResponse,
        val sentOn: LocalDateTime,
        val textContent: String?,
        val offer: OfferResponse? = null
)
