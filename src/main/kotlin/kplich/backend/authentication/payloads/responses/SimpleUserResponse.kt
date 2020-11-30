package kplich.backend.authentication.payloads.responses

import io.swagger.annotations.ApiModel

@ApiModel(description = "a response containing basic information about a user")
data class SimpleUserResponse(
        val id: Long,
        val ethereumAddress: String?,
        val username: String
)
