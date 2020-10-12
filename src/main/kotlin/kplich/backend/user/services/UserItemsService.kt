package kplich.backend.user.services

import kplich.backend.authentication.NoUserLoggedInException
import kplich.backend.authentication.UserWithIdNotFoundException
import kplich.backend.authentication.services.UserService
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
import org.springframework.stereotype.Service

@Service
class UserItemsService(
        private val userRepository: ApplicationUserRepository,
        private val itemRepository: ItemRepository,
        private val conversationRepository: ConversationRepository
) {
    @Throws(
            NoUserLoggedInException::class,
            UserWithIdNotFoundException::class
    )
    private fun getLoggedInUser(): ApplicationUser {
        val loggedInId = UserService.getCurrentlyLoggedId()
                ?: throw NoUserLoggedInException()

        return userRepository.findByIdOrThrow(loggedInId, ::UserWithIdNotFoundException)
    }

    @Throws(
            NoUserLoggedInException::class,
            UserWithIdNotFoundException::class
    )
    fun getItemsWantedToSell(): List<ItemWantedToSellResponse> {
        return itemRepository.findByAddedBy(getLoggedInUser())
                .filter { item ->
                    item.conversations.none { conv ->
                        conv.messages.any { mess ->
                            mess.offer?.isAccepted ?: false
                        }
                    }
                }.map { it.toWantedToSellResponse() }
    }

    @Throws(
            NoUserLoggedInException::class,
            UserWithIdNotFoundException::class
    )
    fun getItemsSold(): List<ItemSoldResponse> {
        return itemRepository
                .findByAddedBy(getLoggedInUser())
                .filter { item ->
                    item.conversations.any { conv ->
                        conv.messages.any { mess ->
                            mess.offer?.isAccepted ?: false
                        }
                    }
                }.map { it.toSoldResponse() }
    }

    @Throws(
            NoUserLoggedInException::class,
            UserWithIdNotFoundException::class
    )
    fun getItemsWantedToBuy(): List<ItemWantedToBuyResponse> {
        val loggedInUser = getLoggedInUser()

        return conversationRepository
                .findAllByInterestedUser(loggedInUser)
                .filter { conversation -> conversation.messages.none { it.offer?.isAccepted ?: false } }
                .map { it.item.toWantedToBuyResponse(loggedInUser) }
    }

    @Throws(
            NoUserLoggedInException::class,
            UserWithIdNotFoundException::class
    )
    fun getItemsBought(): List<ItemBoughtResponse> {
        val loggedInUser = getLoggedInUser()

        return conversationRepository.findAllByInterestedUser(loggedInUser)
                .filter { conversation ->
                    conversation.messages.any { it.offer?.isAccepted ?: false }
                }
                .map { it.item.toBoughtResponse(loggedInUser) }
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
}