package kplich.backend.conversation.payloads.responses.offer

import kplich.backend.conversation.entities.offer.OfferStatus
import java.math.BigDecimal

class PlainAdvanceOfferResponse(
        val advance: BigDecimal,
        id: Long,
        price: BigDecimal,
        offerStatus: OfferStatus,
        contractAddress: String? = null
) : OfferResponse(id, price, offerStatus, contractAddress)