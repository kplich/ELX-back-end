package kplich.backend.conversation.payloads.requests

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "a request for accepting an offer")
data class AcceptOfferRequest(
        @ApiModelProperty("Ethereum address of the contract that has been created from the offer")
        val contractAddress: String
)
