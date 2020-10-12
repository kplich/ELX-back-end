package kplich.backend.user

import kplich.backend.user.payloads.responses.items.ItemBoughtResponse
import kplich.backend.user.payloads.responses.items.ItemSoldResponse
import kplich.backend.user.payloads.responses.items.ItemWantedToBuyResponse
import kplich.backend.user.payloads.responses.items.ItemWantedToSellResponse
import kplich.backend.user.services.UserItemsService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/user")
class UserController(
        private val userItemsService: UserItemsService
) {
    @GetMapping("/wantedToSell")
    fun getItemsWantedToSell(): ResponseEntity<List<ItemWantedToSellResponse>> {
        return ResponseEntity.ok(userItemsService.getItemsWantedToSell())
    }

    @GetMapping("/sold")
    fun getItemsSold(): ResponseEntity<List<ItemSoldResponse>> {
        return ResponseEntity.ok(userItemsService.getItemsSold())
    }

    @GetMapping("/wantedToBuy")
    fun getItemsWantedToBuy(): ResponseEntity<List<ItemWantedToBuyResponse>> {
        return ResponseEntity.ok(userItemsService.getItemsWantedToBuy())
    }

    @GetMapping("/bought")
    fun getItemsBought(): ResponseEntity<List<ItemBoughtResponse>> {
        return ResponseEntity.ok(userItemsService.getItemsBought())
    }
}