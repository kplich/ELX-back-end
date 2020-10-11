package kplich.backend.items.services

import kplich.backend.items.entities.Category
import kplich.backend.items.entities.Item
import kplich.backend.items.payloads.responses.CategoryResponse
import kplich.backend.items.payloads.responses.ItemResponse
import kplich.backend.authentication.services.toSimpleResponse

fun Item.toResponse(): ItemResponse {
    return ItemResponse(
            id,
            title,
            description,
            price,
            addedBy.toSimpleResponse(),
            addedOn,
            category.toResponse(),
            usedStatus,
            photos.map { photo -> photo.url },
            closedOn
    )
}

fun Category.toResponse(): CategoryResponse {
    return CategoryResponse(
            id,
            name
    )
}