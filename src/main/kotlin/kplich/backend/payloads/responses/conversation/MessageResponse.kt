package kplich.backend.payloads.responses.conversation

import kplich.backend.payloads.responses.authentication.SimpleUserResponse
import kplich.backend.payloads.responses.conversation.offer.OfferResponse
import java.time.LocalDateTime

data class MessageResponse(
        val id: Long,
        val sendingUser: SimpleUserResponse,
        val sentOn: LocalDateTime,
        val textContent: String?,
        val offer: OfferResponse? = null
)
