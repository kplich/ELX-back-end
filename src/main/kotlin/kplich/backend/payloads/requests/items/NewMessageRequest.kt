package kplich.backend.payloads.requests.items

import kplich.backend.entities.Message
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class NewMessageRequest(
        @get:NotBlank
        @get:Size(max = Message.MESSAGE_CONTENT_MAX_LENGTH, message = Message.MESSAGE_TOO_LONG_MSG)
        val content: String,
        val offer: NewOfferRequest?
)
