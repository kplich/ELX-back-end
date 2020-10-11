package kplich.backend.user

import kplich.backend.user.payloads.responses.FullUserResponse
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
    @GetMapping()
    fun getUserData(): ResponseEntity<FullUserResponse> {
        return ResponseEntity.ok(userItemsService.getUser())
    }
}