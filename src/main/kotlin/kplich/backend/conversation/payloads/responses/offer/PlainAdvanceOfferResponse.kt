package kplich.backend.conversation.payloads.responses.offer

import kplich.backend.conversation.entities.offer.OfferStatus
import java.math.BigDecimal

data class PlainAdvanceOfferResponse(
        val advance: BigDecimal,
        override val id: Long,
        override val price: BigDecimal,
        override val offerStatus: OfferStatus,
        override val contractAddress: String? = null
) : OfferResponse(id, price, offerStatus, contractAddress)
