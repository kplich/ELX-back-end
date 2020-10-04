package kplich.backend.payloads.responses.user

data class SimpleUserResponse(
        val id: Long,
        val ethereumAddress: String?,
        val username: String
)
