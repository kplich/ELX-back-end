package kplich.backend.payloads.responses.items

import kplich.backend.payloads.responses.authentication.SimpleUserResponse
import java.time.LocalDateTime

data class MessageResponse(
        val id: Long,
        val sendingUser: SimpleUserResponse,
        val sentOn: LocalDateTime,
        val textContent: String,
        val offer: OfferResponse?
)
