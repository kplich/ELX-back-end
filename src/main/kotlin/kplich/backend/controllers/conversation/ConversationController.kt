package kplich.backend.controllers.conversation

import kplich.backend.payloads.requests.conversation.AcceptOfferRequest
import kplich.backend.payloads.requests.conversation.NewMessageRequest
import kplich.backend.payloads.responses.conversation.conversation.ConversationResponse
import kplich.backend.services.conversation.MessageService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/items")
class ConversationController(
        val messageService: MessageService
) {

    @GetMapping("/{itemId}/conversation")
    fun getConversation(@PathVariable itemId: Long, @RequestParam(name = "subjectId", required = false) subjectId: Long?): ResponseEntity<ConversationResponse> {
        return if(subjectId != null) {
            ResponseEntity.ok(messageService.getConversation(itemId, subjectId))
        } else {
            ResponseEntity.ok(messageService.getConversation(itemId))
        }
    }

    @PostMapping("/{itemId}/conversation")
    fun sendMessage(
            @PathVariable itemId: Long,
            @RequestBody newMessageRequest: NewMessageRequest,
            @RequestParam(name = "subjectId", required = false) subjectId: Long?
    ): ResponseEntity<ConversationResponse> {
        return if(subjectId != null) {
            ResponseEntity.ok(messageService.sendMessage(itemId, newMessageRequest, subjectId))
        } else {
            ResponseEntity.ok(messageService.sendMessage(itemId, newMessageRequest))
        }
    }

    @PutMapping("/{offerId}/cancel")
    fun cancelOffer(
            @PathVariable offerId: Long
    ): ResponseEntity<ConversationResponse> {
        return ResponseEntity.ok(messageService.cancelOffer(offerId))
    }

    @PutMapping("/{offerId}/decline")
    fun declineOffer(
            @PathVariable offerId: Long
    ): ResponseEntity<ConversationResponse> {
        return ResponseEntity.ok(messageService.declineOffer(offerId))
    }

    @PutMapping("/{offerId}/accept")
    fun acceptOffer(
            @PathVariable offerId: Long,
            @RequestBody offerAcceptanceRequest: AcceptOfferRequest
    ): ResponseEntity<ConversationResponse> {
        return ResponseEntity.ok(messageService.acceptOffer(offerId, offerAcceptanceRequest))
    }
}
