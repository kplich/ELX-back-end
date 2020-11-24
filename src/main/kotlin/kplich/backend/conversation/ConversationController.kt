package kplich.backend.conversation

import io.swagger.annotations.*
import kplich.backend.configurations.errorhandling.ApiError
import kplich.backend.conversation.payloads.requests.AcceptOfferRequest
import kplich.backend.conversation.payloads.requests.NewMessageRequest
import kplich.backend.conversation.payloads.responses.conversation.FullConversationResponse
import kplich.backend.conversation.services.ConversationService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Controller
@RequestMapping("/items")
@Api(
        value = "Conversation controller",
        description = "Controller for getting conversations, sending messages and accepting/declining offers."
)
class ConversationController(
        val conversationService: ConversationService
) {
    @ApiOperation(value = "Get a conversation about an item", response = FullConversationResponse::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully retrieved the conversation"),
            ApiResponse(code = 401, message = "You're not authorized to view this conversation!", response = ApiError::class),
            ApiResponse(code = 403, message = "You're not allowed to access this endpoint!", response = ApiError::class),
            ApiResponse(code = 404, message = "The item or the conversation does not exist!", response = ApiError::class)
    )
    @GetMapping("/{itemId}/conversation", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getConversation(
            @ApiParam("ID of the item about which the conversation should be fetched")
            @PathVariable
            itemId: Long,
            @ApiParam("ID of the user with whom the conversation should be fetched")
            @RequestParam(name = "receipientId", required = false)
            receipientId: Long?
    ): ResponseEntity<FullConversationResponse> {
        return if(receipientId != null) {
            ResponseEntity.ok(conversationService.getConversation(itemId, receipientId))
        } else {
            ResponseEntity.ok(conversationService.getConversation(itemId))
        }
    }

    @ApiOperation(value = "Send a message about an item to a given user", response = FullConversationResponse::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully sent the message"),
            ApiResponse(code = 400, message = "Invalid message request", response = ApiError::class),
            ApiResponse(code = 401, message = "You're not authorized to send messages to this conversation!", response = ApiError::class),
            ApiResponse(code = 403, message = "You're not allowed to access this endpoint!", response = ApiError::class),
            ApiResponse(code = 404, message = "The item or the conversation does not exist!", response = ApiError::class),
            ApiResponse(code = 409, message = "The item is already closed!", response = ApiError::class)
    )
    @PostMapping("/{itemId}/conversation", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun sendMessage(
            @ApiParam("ID of the item about which the conversation should be fetched")
            @PathVariable
            itemId: Long,
            @ApiParam("new message request")
            @Valid @RequestBody(required = true)
            newMessageRequest: NewMessageRequest,
            @ApiParam("ID of the user with whom the conversation should be fetched")
            @RequestParam(name = "receipientId", required = false)
            receipientId: Long?
    ): ResponseEntity<FullConversationResponse> {
        return if(receipientId != null) {
            ResponseEntity.ok(conversationService.sendMessage(itemId, newMessageRequest, receipientId))
        } else {
            ResponseEntity.ok(conversationService.sendMessage(itemId, newMessageRequest))
        }
    }

    @ApiOperation(value = "Cancel an offer", response = FullConversationResponse::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully cancelled the offer."),
            ApiResponse(code = 401, message = "You're not authorized to decline this offer!", response = ApiError::class),
            ApiResponse(code = 403, message = "You're not allowed to access this endpoint!", response = ApiError::class),
            ApiResponse(code = 404, message = "The offer does not exist!", response = ApiError::class),
            ApiResponse(code = 409, message = "The offer can't be cancelled!", response = ApiError::class)
    )
    @PutMapping("/{offerId}/cancel", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun cancelOffer(
            @ApiParam("ID of the offer to be declined")
            @PathVariable offerId: Long
    ): ResponseEntity<FullConversationResponse> {
        return ResponseEntity.ok(conversationService.cancelOffer(offerId))
    }

    @ApiOperation(value = "Decline an offer", response = FullConversationResponse::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully declined the offer."),
            ApiResponse(code = 401, message = "You're not authorized to decline this offer!", response = ApiError::class),
            ApiResponse(code = 403, message = "You're not allowed to access this endpoint!", response = ApiError::class),
            ApiResponse(code = 404, message = "The offer does not exist!", response = ApiError::class),
            ApiResponse(code = 409, message = "The offer can't be answered!", response = ApiError::class)
    )
    @PutMapping("/{offerId}/decline", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun declineOffer(
            @ApiParam("ID of the offer to be declined")
            @PathVariable
            offerId: Long
    ): ResponseEntity<FullConversationResponse> {
        return ResponseEntity.ok(conversationService.declineOffer(offerId))
    }

    @ApiOperation(value = "Accept an offer", response = FullConversationResponse::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully accepted the offer."),
            ApiResponse(code = 400, message = "Invalid acceptance request", response = ApiError::class),
            ApiResponse(code = 401, message = "You're not authorized to accepted this offer!", response = ApiError::class),
            ApiResponse(code = 403, message = "You're not allowed to access this endpoint!", response = ApiError::class),
            ApiResponse(code = 404, message = "The offer does not exist!", response = ApiError::class),
            ApiResponse(code = 409, message = "The offer can't be answered!", response = ApiError::class)
    )
    @PutMapping("/{offerId}/accept", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun acceptOffer(
            @ApiParam("ID of the offer to be accepted")
            @PathVariable
            offerId: Long,
            @ApiParam("offer acceptance request")
            @Valid @RequestBody(required = true)
            offerAcceptanceRequest: AcceptOfferRequest
    ): ResponseEntity<FullConversationResponse> {
        return ResponseEntity.ok(conversationService.acceptOffer(offerId, offerAcceptanceRequest))
    }
}
