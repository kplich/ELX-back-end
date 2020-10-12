package kplich.backend.authentication.payloads.responses

data class SimpleUserResponse(
        val id: Long,
        val ethereumAddress: String?,
        val username: String
)
