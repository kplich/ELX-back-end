package kplich.backend.payloads.responses.user

import kplich.backend.payloads.responses.items.item.ItemSoldByMeResponse
import kplich.backend.payloads.responses.items.item.ItemWantedByMeResponse

data class FullUserResponse(
        val id: Long,
        val ethereumAddress: String?,
        val username: String,
        val itemsSold: List<ItemSoldByMeResponse>,
        val itemsWanted: List<ItemWantedByMeResponse>
)