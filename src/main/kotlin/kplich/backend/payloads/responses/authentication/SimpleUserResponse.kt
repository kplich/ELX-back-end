package kplich.backend.payloads.responses.authentication

data class SimpleUserResponse(
        val id: Long,
        val ethereumAddress: String?,
        val username: String
)
