package kplich.backend.authentication.payloads.requests

import kplich.backend.authentication.entities.ApplicationUser.Companion.ETHEREUM_ADDRESS_LENGTH
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class SetEthereumAddressRequest(
    @get:NotBlank
    @get:Size(min = ETHEREUM_ADDRESS_LENGTH, max = ETHEREUM_ADDRESS_LENGTH)
    val ethereumAddress: String
)
