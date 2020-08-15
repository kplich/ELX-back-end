package kplich.backend.controllers.items

import kplich.backend.payloads.responses.items.ConversationResponse
import kplich.backend.services.items.MessageService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/items")
class MessageController(
        val messageService: MessageService
) {

    @GetMapping("/{itemId}/messages")
    fun getMessages(@PathVariable itemId: Long, @RequestParam(name = "userId", required = false) userId: Long?): ResponseEntity<ConversationResponse> {
        return ResponseEntity.ok(messageService.getConversation(itemId, userId))
    }
}
