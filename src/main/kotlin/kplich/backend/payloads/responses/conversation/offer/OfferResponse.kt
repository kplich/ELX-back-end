package kplich.backend.payloads.responses.conversation.offer

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import kplich.backend.entities.conversation.offer.OfferStatus
import java.math.BigDecimal

@JsonTypeInfo(
        use = JsonTypeInfo.Id.MINIMAL_CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        JsonSubTypes.Type(value = PlainAdvanceOfferResponse::class, name = "PlainAdvanceOfferResponse"),
        JsonSubTypes.Type(value = DoubleAdvanceOfferResponse::class, name = "DoubleAdvanceOfferResponse")
)
abstract class OfferResponse(
        val id: Long,
        val price: BigDecimal,
        val offerStatus: OfferStatus,
        val contractAddress: String? = null
)