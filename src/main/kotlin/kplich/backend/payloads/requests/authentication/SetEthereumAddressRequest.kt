package kplich.backend.payloads.requests.authentication

import kplich.backend.entities.user.ApplicationUser.Companion.ETHEREUM_ADDRESS_LENGTH
import javax.validation.constraints.Size

data class SetEthereumAddressRequest(
    @get:Size(min = ETHEREUM_ADDRESS_LENGTH, max = ETHEREUM_ADDRESS_LENGTH)
    val ethereumAddress: String
)
