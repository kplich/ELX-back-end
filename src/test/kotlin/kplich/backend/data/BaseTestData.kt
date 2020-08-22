package kplich.backend.data

import kplich.backend.payloads.responses.authentication.SimpleUserResponse
import kplich.backend.payloads.responses.items.CategoryResponse

abstract class BaseTestData {
    protected val categories
        get(): Map<Int, CategoryResponse> = mapOf(
                1 to CategoryResponse(1, "House and Garden"),
                2 to CategoryResponse(2, "Electronics"),
                3 to CategoryResponse(3, "Fashion")
        )

    protected val users
        get(): Map<Int, SimpleUserResponse> = mapOf(
                1 to SimpleUserResponse(1, "kplich"),
                2 to SimpleUserResponse(2, "kplich2"),
                3 to SimpleUserResponse(3, "kplich3")
        )
}