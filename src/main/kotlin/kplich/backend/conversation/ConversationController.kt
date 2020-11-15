package kplich.backend.conversation

import kplich.backend.conversation.payloads.requests.AcceptOfferRequest
import kplich.backend.conversation.payloads.requests.NewMessageRequest
import kplich.backend.conversation.payloads.responses.conversation.FullConversationResponse
import kplich.backend.conversation.services.ConversationService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/items")
class ConversationController(
        val conversationService: ConversationService
) {

    @GetMapping("/{itemId}/conversation")
    fun getConversation(
            @PathVariable itemId: Long,
            @RequestParam(
                    name = "receipientId",
                    required = false) receipientId: Long?
    ): ResponseEntity<FullConversationResponse> {
        return if(receipientId != null) {
            ResponseEntity.ok(conversationService.getConversation(itemId, receipientId))
        } else {
            ResponseEntity.ok(conversationService.getConversation(itemId))
        }
    }

    @PostMapping("/{itemId}/conversation")
    fun sendMessage(
            @PathVariable itemId: Long,
            @RequestBody newMessageRequest: NewMessageRequest,
            @RequestParam(name = "receipientId", required = false) receipientId: Long?
    ): ResponseEntity<FullConversationResponse> {
        return if(receipientId != null) {
            ResponseEntity.ok(conversationService.sendMessage(itemId, newMessageRequest, receipientId))
        } else {
            ResponseEntity.ok(conversationService.sendMessage(itemId, newMessageRequest))
        }
    }

    @PutMapping("/{offerId}/cancel")
    fun cancelOffer(
            @PathVariable offerId: Long
    ): ResponseEntity<FullConversationResponse> {
        return ResponseEntity.ok(conversationService.cancelOffer(offerId))
    }

    @PutMapping("/{offerId}/decline")
    fun declineOffer(
            @PathVariable offerId: Long
    ): ResponseEntity<FullConversationResponse> {
        return ResponseEntity.ok(conversationService.declineOffer(offerId))
    }

    @PutMapping("/{offerId}/accept")
    fun acceptOffer(
            @PathVariable offerId: Long,
            @RequestBody offerAcceptanceRequest: AcceptOfferRequest
    ): ResponseEntity<FullConversationResponse> {
        return ResponseEntity.ok(conversationService.acceptOffer(offerId, offerAcceptanceRequest))
    }
}
