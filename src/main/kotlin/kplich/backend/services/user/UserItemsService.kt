package kplich.backend.services.user

import kplich.backend.entities.conversation.Conversation
import kplich.backend.entities.conversation.Message
import kplich.backend.entities.items.Category
import kplich.backend.entities.items.Item
import kplich.backend.entities.user.ApplicationUser
import kplich.backend.exceptions.NoUserLoggedInException
import kplich.backend.exceptions.UserWithIdNotFoundException
import kplich.backend.payloads.responses.conversation.conversation.SimpleConversationResponse
import kplich.backend.payloads.responses.conversation.message.SimpleMessageResponse
import kplich.backend.payloads.responses.items.CategoryResponse
import kplich.backend.payloads.responses.items.item.ItemSoldByMeResponse
import kplich.backend.payloads.responses.items.item.ItemWantedByMeResponse
import kplich.backend.payloads.responses.user.FullUserResponse
import kplich.backend.payloads.responses.user.SimpleUserResponse
import kplich.backend.repositories.conversation.ConversationRepository
import kplich.backend.repositories.findByIdOrThrow
import kplich.backend.repositories.items.ItemRepository
import kplich.backend.repositories.user.ApplicationUserRepository
import kplich.backend.services.ResponseConverter.categoryToResponse
import kplich.backend.services.ResponseConverter.offerToResponse
import kplich.backend.services.ResponseConverter.userToSimpleResponse
import org.springframework.stereotype.Service

@Service
class UserItemsService(
        private val userRepository: ApplicationUserRepository,
        private val itemRepository: ItemRepository,
        private val conversationRepository: ConversationRepository
) {

    @Throws(
            NoUserLoggedInException::class
    )
    fun getUser(): FullUserResponse {
        val loggedInId = UserService.getCurrentlyLoggedId()
                ?: throw NoUserLoggedInException()

        val user = userRepository.findByIdOrThrow(loggedInId, ::UserWithIdNotFoundException)

        return user.toFullResponse()
    }

    private fun ApplicationUser.toFullResponse(): FullUserResponse {
        val itemsSold = itemRepository.findByAddedBy(this).map {
            it.toSoldByMeResponse()
        }

        val itemsWanted = conversationRepository.findAllByInterestedUser(this).map {
            it.item.toWantedByMeResponse(this)
        }

        return FullUserResponse(
                id = id,
                ethereumAddress = ethereumAddress,
                username = username,
                itemsSold = itemsSold,
                itemsWanted = itemsWanted
        )
    }

    private fun Item.toSoldByMeResponse(): ItemSoldByMeResponse {
        return ItemSoldByMeResponse(
                id = id,
                title = title,
                description = description,
                price = price,
                addedOn = addedOn,
                category = category.toResponse(),
                usedStatus = usedStatus,
                photoUrl = photos[0].url,
                conversations = conversations.map { it.toSimpleResponse() }
        )
    }

    private fun Item.toWantedByMeResponse(interestedUser: ApplicationUser): ItemWantedByMeResponse {
        return ItemWantedByMeResponse(
                id = id,
                title = title,
                description = description,
                price = price,
                addedBy = addedBy.toSimpleResponse(),
                addedOn = addedOn,
                category = category.toResponse(),
                usedStatus = usedStatus,
                photoUrl = photos[0].url,
                conversation = conversations.filter {
                    it.interestedUser == interestedUser
                }.map {
                    it.toSimpleResponse()
                }[0]
        )
    }

    private fun Category.toResponse(): CategoryResponse = categoryToResponse(this)

    private fun ApplicationUser.toSimpleResponse(): SimpleUserResponse = userToSimpleResponse(this)

    private fun Conversation.toSimpleResponse(): SimpleConversationResponse {
        val lastMessage = this.messages.maxByOrNull { it.sentOn }!!
        val lastOffer = this.messages
                .filter { it.offer != null }
                .maxByOrNull { it.sentOn }
                ?.offer

        return SimpleConversationResponse(
                id = id,
                interestedUser = interestedUser.toSimpleResponse(),
                lastMessage = lastMessage.toSimpleResponse(),
                lastOffer = lastOffer?.let { offerToResponse(it) }
        )
    }

    private fun Message.toSimpleResponse(): SimpleMessageResponse {
        return SimpleMessageResponse(
                id = id,
                sendingUser = sender.toSimpleResponse(),
                sentOn = sentOn,
                textContent = content
        )
    }
}