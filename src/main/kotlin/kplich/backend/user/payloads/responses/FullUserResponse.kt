package kplich.backend.user.payloads.responses

import kplich.backend.user.payloads.responses.items.ItemBoughtResponse
import kplich.backend.user.payloads.responses.items.ItemSoldResponse
import kplich.backend.user.payloads.responses.items.ItemWantedToSellResponse
import kplich.backend.user.payloads.responses.items.ItemWantedToBuyResponse

data class FullUserResponse(
        val id: Long,
        val ethereumAddress: String?,
        val username: String,
        val itemsWantedToSell: List<ItemWantedToSellResponse>,
        val itemsSold: List<ItemSoldResponse>,
        val itemsWantedToBuy: List<ItemWantedToBuyResponse>,
        val itemsBought: List<ItemBoughtResponse>
)