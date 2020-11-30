package kplich.backend.user

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import kplich.backend.configurations.errorhandling.ApiError
import kplich.backend.items.payloads.responses.ItemResponse
import kplich.backend.user.payloads.responses.items.ItemBoughtResponse
import kplich.backend.user.payloads.responses.items.ItemSoldResponse
import kplich.backend.user.payloads.responses.items.ItemWantedToBuyResponse
import kplich.backend.user.payloads.responses.items.ItemWantedToSellResponse
import kplich.backend.user.services.UserItemsService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Api(value = "User Controller", description = "Controller for getting detailed data about a user")
@Controller
@RequestMapping("/user")
class UserController(
        private val userItemsService: UserItemsService
) {
    @ApiOperation(value = "Get items that the user wants to sell")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully fetched the items!"),
            ApiResponse(code = 403, message = "You're not allowed to access this endpoint!", response = ApiError::class)
    )
    @GetMapping("/wantedToSell", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getItemsWantedToSell(): ResponseEntity<List<ItemWantedToSellResponse>> {
        return ResponseEntity.ok(userItemsService.getItemsWantedToSell())
    }

    @ApiOperation(value = "Get items that the user has already sold")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully fetched the items!"),
            ApiResponse(code = 403, message = "You're not allowed to access this endpoint!", response = ApiError::class)
    )
    @GetMapping("/sold", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getItemsSold(): ResponseEntity<List<ItemSoldResponse>> {
        return ResponseEntity.ok(userItemsService.getItemsSold())
    }

    @ApiOperation(value = "Get items that the user wants to buy (has sent a message to the owner")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully fetched the items!"),
            ApiResponse(code = 403, message = "You're not allowed to access this endpoint!", response = ApiError::class)
    )
    @GetMapping("/wantedToBuy", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getItemsWantedToBuy(): ResponseEntity<List<ItemWantedToBuyResponse>> {
        return ResponseEntity.ok(userItemsService.getItemsWantedToBuy())
    }

    @ApiOperation(value = "Get items that the user has already bought")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully fetched the items!"),
            ApiResponse(code = 403, message = "You're not allowed to access this endpoint!", response = ApiError::class)
    )
    @GetMapping("/bought", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getItemsBought(): ResponseEntity<List<ItemBoughtResponse>> {
        return ResponseEntity.ok(userItemsService.getItemsBought())
    }
}