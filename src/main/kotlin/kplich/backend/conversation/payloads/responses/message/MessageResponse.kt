package kplich.backend.conversation.payloads.responses.message

import kplich.backend.authentication.payloads.responses.SimpleUserResponse
import kplich.backend.conversation.payloads.responses.offer.OfferResponse
import java.time.LocalDateTime

data class MessageResponse(
        val id: Long,
        val sendingUser: SimpleUserResponse,
        val sentOn: LocalDateTime,
        val textContent: String?,
        val offer: OfferResponse? = null
)
