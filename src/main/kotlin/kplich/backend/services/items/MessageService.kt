package kplich.backend.services.items

import kplich.backend.entities.Conversation
import kplich.backend.exceptions.items.*
import kplich.backend.payloads.responses.items.ConversationResponse
import kplich.backend.repositories.ApplicationUserRepository
import kplich.backend.repositories.findByIdOrThrow
import kplich.backend.repositories.items.ItemRepository
import kplich.backend.services.ResponseService
import kplich.backend.services.UserService
import org.springframework.stereotype.Service

@Service
class MessageService(
        val itemRepository: ItemRepository,
        val userRepository: ApplicationUserRepository,
        val conversationRepository: ConversationRepository,
        val userService: UserService,
        val responseService: ResponseService
) {
    fun getConversation(itemId: Long, userId: Long?): ConversationResponse {
        val item = itemRepository.findByIdOrThrow(itemId, ::ItemNotFoundException)

        val currentlyLoggedId = UserService.getCurrentlyLoggedId()

        // owner of the item wants to get a conversation
        if (item.addedBy.id == currentlyLoggedId) {
            // conversation subject must be specified
            if (userId == null) throw NoUserIdProvided()

            val interestedUser = userRepository.findByIdOrThrow(userId, ::UserIdNotFoundException)

            return conversationRepository
                    .findByInterestedUserAndItem(interestedUser, item)
                    ?.toResponse()
                    ?: throw NoConversationFound(itemId, userId)
        }
        // currently logged user wants to get a conversation
        else {
            val interestedUser = userRepository.findByIdOrThrow(currentlyLoggedId, ::UserIdNotFoundException)

            return conversationRepository
                    .findByInterestedUserAndItem(interestedUser, item)
                    ?.toResponse()
                    ?: throw NoConversationFound(itemId, currentlyLoggedId)
        }
    }

    private fun Conversation.toResponse() = responseService.conversationToResponse(this)
}
