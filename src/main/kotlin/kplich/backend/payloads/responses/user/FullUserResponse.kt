package kplich.backend.payloads.responses.user

import kplich.backend.payloads.responses.items.item.ItemBoughtResponse
import kplich.backend.payloads.responses.items.item.ItemSoldResponse
import kplich.backend.payloads.responses.items.item.ItemWantedToSellResponse
import kplich.backend.payloads.responses.items.item.ItemWantedToBuyResponse

data class FullUserResponse(
        val id: Long,
        val ethereumAddress: String?,
        val username: String,
        val itemsWantedToSell: List<ItemWantedToSellResponse>,
        val itemsSold: List<ItemSoldResponse>,
        val itemsWantedToBuy: List<ItemWantedToBuyResponse>,
        val itemsBought: List<ItemBoughtResponse>
)