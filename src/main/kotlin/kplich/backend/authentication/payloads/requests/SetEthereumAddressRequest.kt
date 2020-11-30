package kplich.backend.authentication.payloads.requests

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import kplich.backend.authentication.entities.ApplicationUser.Companion.ETHEREUM_ADDRESS_LENGTH
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@ApiModel(description = "a request for setting the user's Ethereum address")
data class SetEthereumAddressRequest(
    @get:NotBlank
    @get:Size(min = ETHEREUM_ADDRESS_LENGTH, max = ETHEREUM_ADDRESS_LENGTH)
    val ethereumAddress: String
)
