package kplich.backend.controllers.user

import kplich.backend.payloads.responses.user.FullUserResponse
import kplich.backend.services.user.UserItemsService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/user")
class UserController(
        private val userItemsService: UserItemsService
) {
    @GetMapping()
    fun getUserData(): ResponseEntity<FullUserResponse> {
        return ResponseEntity.ok(userItemsService.getUser())
    }
}