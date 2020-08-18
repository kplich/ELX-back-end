package kplich.backend.payloads.responses.items

import kplich.backend.entities.OfferStatus
import kplich.backend.entities.OfferType
import java.math.BigDecimal

data class OfferResponse(
        val id: Long,
        val type: OfferType,
        val price: BigDecimal,
        val advance: BigDecimal,
        val offerStatus: OfferStatus,
        val contractAddress: String? = null
)
