package kplich.backend.controllers

import kplich.backend.payloads.requests.items.ItemAddRequest
import kplich.backend.payloads.requests.items.ItemSearchingCriteria
import kplich.backend.payloads.requests.items.ItemUpdateRequest
import kplich.backend.payloads.responses.items.ItemResponse
import kplich.backend.services.ItemService
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
    fun getAllItems(@Valid @RequestBody(required = false) searchingCriteria: ItemSearchingCriteria?): ResponseEntity<List<ItemResponse>> {
        return ResponseEntity.ok(itemService.getAllItems(searchingCriteria))
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun addItem(@RequestBody @Valid itemAddRequest: ItemAddRequest): ResponseEntity<ItemResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.addItem(itemAddRequest))
    }
}
