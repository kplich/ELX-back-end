package kplich.backend.user.services

import kplich.backend.authentication.NoUserLoggedInException
import kplich.backend.authentication.UserWithIdNotFoundException
import kplich.backend.authentication.services.UserService
import kplich.backend.authentication.services.toSimpleResponse
import kplich.backend.conversation.ConversationNotFoundException
import kplich.backend.items.entities.Item
import kplich.backend.authentication.entities.ApplicationUser
import kplich.backend.user.payloads.responses.items.ItemBoughtResponse
import kplich.backend.user.payloads.responses.items.ItemSoldResponse
import kplich.backend.user.payloads.responses.items.ItemWantedToBuyResponse
import kplich.backend.user.payloads.responses.items.ItemWantedToSellResponse
import kplich.backend.user.payloads.responses.FullUserResponse
import kplich.backend.conversation.repositories.ConversationRepository
import kplich.backend.core.findByIdOrThrow
import kplich.backend.items.repositories.ItemRepository
import kplich.backend.authentication.repositories.ApplicationUserRepository
import kplich.backend.conversation.services.toResponse
import kplich.backend.items.services.toResponse
import kplich.backend.user.NoAcceptedOfferFound
import kplich.backend.user.NoConversationWithBuyerException
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
                addedBy = addedBy.toSimpleResponse(),
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
                addedBy = addedBy.toSimpleResponse(),
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
}