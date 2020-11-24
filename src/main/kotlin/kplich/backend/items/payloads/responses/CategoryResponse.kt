package kplich.backend.items.payloads.responses

import io.swagger.annotations.ApiModel

@ApiModel(description = "a response containing category data")
data class CategoryResponse(
        val id: Int,
        val name: String
)
