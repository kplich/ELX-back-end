package kplich.backend.controllers

import kplich.backend.exceptions.BadAddItemRequestException
import kplich.backend.exceptions.ItemAlreadyClosedException
import kplich.backend.exceptions.ItemNotFoundException
import kplich.backend.exceptions.UnauthorizedItemClosingRequest
import kplich.backend.payloads.requests.ItemAddRequest
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

    @PutMapping("/{id}/close")
    fun closeItem(@PathVariable id: Long): ResponseEntity<*> {
        return try {
            ResponseEntity.ok(itemService.closeItem(id))
        } catch (e: ItemNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: UnauthorizedItemClosingRequest) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.message)
        } catch (e: ItemAlreadyClosedException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
        }
    }

    @GetMapping("/{id}")
    fun getItem(@PathVariable id: Long): ResponseEntity<*> {
        return try {
            ResponseEntity.ok(itemService.getItem(id))
        } catch (e: ItemNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
    }

    @PostMapping("/")
    fun addItem(@Valid @RequestBody itemAddRequest: ItemAddRequest): ResponseEntity<*> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(itemService.addItem(itemAddRequest))
        } catch (e: BadAddItemRequestException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
    }
}
