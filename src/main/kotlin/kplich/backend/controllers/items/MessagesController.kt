package kplich.backend.controllers.items

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("/items")
class MessagesController {

    @GetMapping("/{itemId}/messages")
    fun getMessages(@PathVariable itemId: Long, @RequestParam(name = "conversationId", required = false) conversationId: Long?) {

    }
}
