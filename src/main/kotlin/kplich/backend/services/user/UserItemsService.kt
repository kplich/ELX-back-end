package kplich.backend.services.user

import kplich.backend.entities.conversation.Conversation
import kplich.backend.entities.conversation.Message
import kplich.backend.entities.conversation.offer.Offer
import kplich.backend.entities.items.Category
import kplich.backend.entities.items.Item
import kplich.backend.entities.user.ApplicationUser
import kplich.backend.exceptions.*
import kplich.backend.payloads.responses.conversation.conversation.SimpleConversationResponse
import kplich.backend.payloads.responses.conversation.message.SimpleMessageResponse
import kplich.backend.payloads.responses.conversation.offer.OfferResponse
import kplich.backend.payloads.responses.items.CategoryResponse
import kplich.backend.payloads.responses.items.item.ItemBoughtResponse
import kplich.backend.payloads.responses.items.item.ItemSoldResponse
import kplich.backend.payloads.responses.items.item.ItemWantedToBuyResponse
import kplich.backend.payloads.responses.items.item.ItemWantedToSellResponse
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
        val wantedToSellAndSold = itemRepository.findByAddedBy(this).partition { item ->
            item.conversations.none { conv ->
                conv.messages.any { mess ->
                    mess.offer?.isAccepted ?: false
                }
            }
        }

        val wantedToSell = wantedToSellAndSold.first.map { it.toWantedToSellResponse() }

        val sold = wantedToSellAndSold.second.map { it.toSoldResponse() }

        val itemsWanted = conversationRepository
                .findAllByInterestedUser(this)
                .filter { conversation -> conversation.messages.none { it.offer?.isAccepted ?: false } }
                .map { it.item.toWantedToBuyResponse(this) }

        val itemsBought = conversationRepository.findAllByInterestedUser(this)
                .filter { conversation ->
                    conversation.messages.any { it.offer?.isAccepted ?: false }
                }
                .map { it.item.toBoughtResponse(this) }

        return FullUserResponse(
                id = id,
                ethereumAddress = ethereumAddress,
                username = username,
                itemsWantedToSell = wantedToSell,
                itemsSold = sold,
                itemsWantedToBuy = itemsWanted,
                itemsBought = itemsBought
        )
    }

    // TODO: put it into Response Converter
    private fun Item.toWantedToSellResponse(): ItemWantedToSellResponse {
        return ItemWantedToSellResponse(
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

    private fun Item.toSoldResponse(): ItemSoldResponse {
        val conversationWithBuyer = conversations.find {
            it.messages.any { message -> message.offer?.isAccepted ?: false }
        } ?: throw NoConversationWithBuyerException(id)

        val messageWithAcceptedOffer = conversationWithBuyer.messages.find {
            it.offer?.isAccepted ?: false
        } ?: throw NoAcceptedOfferFound(id, conversationWithBuyer.id)


        return ItemSoldResponse(
                id = id,
                title = title,
                description = description,
                price = price,
                addedOn = addedOn,
                category = category.toResponse(),
                usedStatus = usedStatus,
                photoUrl = photos[0].url,
                offer = messageWithAcceptedOffer.offer!!.toResponse()
        )
    }

    private fun Item.toWantedToBuyResponse(interestedUser: ApplicationUser): ItemWantedToBuyResponse {
        return ItemWantedToBuyResponse(
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

    private fun Item.toBoughtResponse(interestedUser: ApplicationUser): ItemBoughtResponse {
        return ItemBoughtResponse(
                id = id,
                title = title,
                description = description,
                price = price,
                addedBy = addedBy.toSimpleResponse(),
                addedOn = addedOn,
                category = category.toResponse(),
                usedStatus = usedStatus,
                photoUrl = photos[0].url,
                offer = conversations.find { it.interestedUser == interestedUser }
                        ?.messages?.find { it.offer?.isAccepted ?: false }
                        ?.offer?.toResponse()
                        ?: throw ConversationNotFoundException(id, interestedUser.id)
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

    private fun Offer.toResponse(): OfferResponse = offerToResponse(this)
}