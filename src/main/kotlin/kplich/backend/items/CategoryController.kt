package kplich.backend.items

import kplich.backend.items.payloads.requests.ItemAddRequest
import kplich.backend.items.payloads.requests.ItemFilteringCriteria
import kplich.backend.items.payloads.requests.ItemUpdateRequest
import kplich.backend.items.payloads.responses.CategoryResponse
import kplich.backend.items.payloads.responses.ItemResponse
import kplich.backend.items.services.ItemService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Controller
@RequestMapping("/items")
class ItemController(
        private val itemService: ItemService
) {
    @PutMapping("/")
    @ResponseStatus(HttpStatus.OK)
    fun updateItem(@Valid @RequestBody itemUpdateRequest: ItemUpdateRequest): ResponseEntity<ItemResponse> {
        return ResponseEntity.ok(itemService.updateItem(itemUpdateRequest))
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/close")
    fun closeItem(@PathVariable id: Long): ResponseEntity<ItemResponse> {
        return ResponseEntity.ok(itemService.closeItem(id))
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getItem(@PathVariable id: Long): ResponseEntity<ItemResponse> {
        return ResponseEntity.ok(itemService.getItem(id))
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun getAllItems(@Valid @RequestBody(required = false) filteringCriteria: ItemFilteringCriteria?): ResponseEntity<List<ItemResponse>> {
        return ResponseEntity.ok(itemService.getAllOpenItems(filteringCriteria))
    }

    @PostMapping("/")
    fun addItem(@Valid @RequestBody itemAddRequest: ItemAddRequest): ResponseEntity<ItemResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.addItem(itemAddRequest))
    }
}

@Controller
@RequestMapping("/categories")
class CategoryController(
        private val itemService: ItemService
) {
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun getCategories(): ResponseEntity<List<CategoryResponse>> {
        return ResponseEntity.ok(itemService.getCategories())
    }
}