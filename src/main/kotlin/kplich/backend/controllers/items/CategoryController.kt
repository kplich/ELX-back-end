package kplich.backend.controllers.items

import kplich.backend.payloads.responses.items.CategoryResponse
import kplich.backend.services.ItemService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

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
