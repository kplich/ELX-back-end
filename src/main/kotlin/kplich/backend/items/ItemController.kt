package kplich.backend.items

import io.swagger.annotations.*
import kplich.backend.configurations.errorhandling.ApiError
import kplich.backend.conversation.payloads.responses.conversation.FullConversationResponse
import kplich.backend.items.payloads.requests.ItemAddRequest
import kplich.backend.items.payloads.requests.ItemFilteringCriteria
import kplich.backend.items.payloads.requests.ItemUpdateRequest
import kplich.backend.items.payloads.responses.CategoryResponse
import kplich.backend.items.payloads.responses.ItemResponse
import kplich.backend.items.services.ItemService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Api(value = "Item Controller", description = "Controller for getting and adding items.")
@Controller
@RequestMapping("/items")
class ItemController(
        private val itemService: ItemService
) {
    @ApiOperation(value = "Update information about the item", response = ItemResponse::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully updated the item"),
            ApiResponse(code = 400, message = "Invalid item data", response = ApiError::class),
            ApiResponse(code = 401, message = "You're not authorized to update this item!", response = ApiError::class),
            ApiResponse(code = 403, message = "You're not allowed to access this endpoint!", response = ApiError::class),
            ApiResponse(code = 404, message = "The item does not exist!", response = ApiError::class),
            ApiResponse(code = 409, message = "The item is already closed!", response = ApiError::class)
    )
    @PutMapping("", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun updateItem(
            @ApiParam("a request for updating the item")
            @Valid @RequestBody(required = true)
            itemUpdateRequest: ItemUpdateRequest
    ): ResponseEntity<ItemResponse> {
        return ResponseEntity.ok(itemService.updateItem(itemUpdateRequest))
    }

    @ApiOperation(value = "Close the item", response = ItemResponse::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully closed the item"),
            ApiResponse(code = 401, message = "You're not authorized to close this item!", response = ApiError::class),
            ApiResponse(code = 403, message = "You're not allowed to access this endpoint!", response = ApiError::class),
            ApiResponse(code = 404, message = "The item does not exist!", response = ApiError::class),
            ApiResponse(code = 409, message = "The item is already closed!", response = ApiError::class)
    )
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/close", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun closeItem(
            @ApiParam("ID of the item to be closed")
            @PathVariable id: Long
    ): ResponseEntity<ItemResponse> {
        return ResponseEntity.ok(itemService.closeItem(id))
    }

    @ApiOperation(value = "Retrieve the information about an item", response = ItemResponse::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully read the item"),
            ApiResponse(code = 404, message = "The item does not exist!", response = ApiError::class)
    )
    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getItem(
            @ApiParam("ID of the item to be fetched")
            @PathVariable id: Long
    ): ResponseEntity<ItemResponse> {
        return ResponseEntity.ok(itemService.getItem(id))
    }

    @ApiOperation(value = "Retrieve the information about all open items")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully read the items"),
    )
    @GetMapping("", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getAllItems(
            @Valid @RequestBody(required = false)
            filteringCriteria: ItemFilteringCriteria?
    ): ResponseEntity<List<ItemResponse>> {
        return ResponseEntity.ok(itemService.getAllOpenItems(filteringCriteria))
    }

    @ApiOperation(value = "Close the item", response = ItemResponse::class)
    @ApiResponses(
            ApiResponse(code = 201, message = "Successfully created the item"),
            ApiResponse(code = 400, message = "Invalid request data!", response = ApiError::class),
            ApiResponse(code = 403, message = "You're not allowed to access this endpoint!", response = ApiError::class)
    )
    @PostMapping("", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addItem(
            @Valid @RequestBody
            itemAddRequest: ItemAddRequest
    ): ResponseEntity<ItemResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.addItem(itemAddRequest))
    }
}

@Api(value = "Category Controller", description = "Controller for getting item categories.")
@Controller
@RequestMapping("/categories")
class CategoryController(
        private val itemService: ItemService
) {
    @ApiOperation(value = "Retrieve the information about all item categories")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully read the items"),
    )
    @GetMapping("", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getCategories(): ResponseEntity<List<CategoryResponse>> {
        return ResponseEntity.ok(itemService.getCategories())
    }
}