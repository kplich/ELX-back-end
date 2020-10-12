package kplich.backend.data

import kplich.backend.authentication.payloads.responses.SimpleUserResponse
import kplich.backend.items.payloads.responses.CategoryResponse

abstract class BaseTestData {
    protected val categories
        get(): Map<Int, CategoryResponse> = mapOf(
                1 to CategoryResponse(1, "House and Garden"),
                2 to CategoryResponse(2, "Electronics"),
                3 to CategoryResponse(3, "Fashion")
        )

    protected val users
        get(): Map<Int, SimpleUserResponse> = mapOf(
                1 to SimpleUserResponse(1, "0xc1912fee45d61c87cc5ea59dae31190fffff232d", "kplich"),
                2 to SimpleUserResponse(2, "0x06012c8cf97bead5deae237070f9587f8e7a266d", "kplich2"),
                3 to SimpleUserResponse(3, "0x5e97870f263700f46aa00d967821199b9bc5a120", "kplich3"),
                4 to SimpleUserResponse(4, null, "kplich4")
        )
}
