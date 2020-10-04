package kplich.backend.controllers.items

import kplich.backend.payloads.requests.items.ItemAddRequest
import kplich.backend.payloads.requests.items.ItemFilteringCriteria
import kplich.backend.payloads.requests.items.ItemUpdateRequest
import kplich.backend.payloads.responses.items.item.ItemResponse
import kplich.backend.services.items.ItemService
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
    @ResponseStatus(HttpStatus.CREATED)
    fun addItem(@RequestBody @Valid itemAddRequest: ItemAddRequest): ResponseEntity<ItemResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.addItem(itemAddRequest))
    }
}
