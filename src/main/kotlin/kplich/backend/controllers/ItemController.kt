package kplich.backend.controllers

import kplich.backend.exceptions.BadAddItemRequestException
import kplich.backend.payloads.requests.ItemAddRequest
import kplich.backend.services.ItemService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid

@Controller
@RequestMapping("/items")
class ItemController(
        private val itemService: ItemService
) {

    @PostMapping("/")
    fun addItem(@Valid @RequestBody itemAddRequest: ItemAddRequest): ResponseEntity<*> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(itemService.addItem(itemAddRequest))
        } catch (e: BadAddItemRequestException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)
        }
    }
}
