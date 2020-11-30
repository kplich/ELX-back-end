package kplich.backend.conversation.payloads.responses.offer

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import kplich.backend.conversation.entities.offer.OfferStatus
import java.math.BigDecimal

@ApiModel(description = "response containing offer data")
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
        open val id: Long,
        open val price: BigDecimal,
        open val offerStatus: OfferStatus,
        @ApiModelProperty("address of the contract associated with the offer")
        open val contractAddress: String? = null
)
