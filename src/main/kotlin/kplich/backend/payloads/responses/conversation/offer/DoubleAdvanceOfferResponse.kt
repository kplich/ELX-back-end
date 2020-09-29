package kplich.backend.payloads.responses.conversation.offer

import kplich.backend.entities.conversation.offer.OfferStatus
import java.math.BigDecimal

class DoubleAdvanceOfferResponse(
        id: Long,
        price: BigDecimal,
        offerStatus: OfferStatus,
        contractAddress: String? = null
) : OfferResponse(id, price, offerStatus, contractAddress)