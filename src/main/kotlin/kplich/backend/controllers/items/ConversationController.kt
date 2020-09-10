package kplich.backend.controllers.items

import kplich.backend.payloads.requests.items.AcceptOfferRequest
import kplich.backend.payloads.requests.items.NewMessageRequest
import kplich.backend.payloads.responses.items.ConversationResponse
import kplich.backend.services.items.MessageService
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
        return ResponseEntity.ok(messageService.getConversation(itemId, subjectId))
    }

    @PostMapping("/{itemId}/conversation")
    fun sendMessage(
            @PathVariable itemId: Long,
            @RequestBody newMessageRequest: NewMessageRequest,
            @RequestParam(name = "subjectId", required = false) subjectId: Long?
    ): ResponseEntity<ConversationResponse> {
        return ResponseEntity.ok(messageService.sendMessage(itemId, newMessageRequest, subjectId))
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
