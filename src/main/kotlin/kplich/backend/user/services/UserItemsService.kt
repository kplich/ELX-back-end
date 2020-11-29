package kplich.backend.user.services

import kplich.backend.authentication.NoUserLoggedInException
import kplich.backend.authentication.UserWithIdNotFoundException
import kplich.backend.authentication.services.UserService
import kplich.backend.conversation.services.ConversationService
import kplich.backend.items.services.ItemService
import kplich.backend.user.payloads.responses.items.ItemBoughtResponse
import kplich.backend.user.payloads.responses.items.ItemSoldResponse
import kplich.backend.user.payloads.responses.items.ItemWantedToBuyResponse
import kplich.backend.user.payloads.responses.items.ItemWantedToSellResponse
import org.springframework.stereotype.Service

@Service
class UserItemsService(
        private val userService: UserService,
        private val itemService: ItemService,
        private val conversationService: ConversationService
) {

    @Throws(
            NoUserLoggedInException::class,
            UserWithIdNotFoundException::class
    )
    fun getItemsWantedToSell(): List<ItemWantedToSellResponse> {
        val loggedInUser = userService.getLoggedInUser()

        return itemService.getAllItemsAddedBy(loggedInUser)
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
        val loggedInUser = userService.getLoggedInUser()


        return itemService.getAllItemsAddedBy(loggedInUser)
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
        val loggedInUser = userService.getLoggedInUser()


        return conversationService.getConversationsWithInterestedUser(loggedInUser)
                .filter { conversation -> conversation.messages.none { it.offer?.isAccepted ?: false } }
                .map { it.item.toWantedToBuyResponse(loggedInUser) }
    }

    @Throws(
            NoUserLoggedInException::class,
            UserWithIdNotFoundException::class
    )
    fun getItemsBought(): List<ItemBoughtResponse> {
        val loggedInUser = userService.getLoggedInUser()

        return conversationService.getConversationsWithInterestedUser(loggedInUser)
                .filter { conversation ->
                    conversation.messages.any { it.offer?.isAccepted ?: false }
                }
                .map { it.item.toBoughtResponse(loggedInUser) }
    }
}
