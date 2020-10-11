package kplich.backend.user.services

import kplich.backend.authentication.entities.ApplicationUser
import kplich.backend.authentication.services.toSimpleResponse
import kplich.backend.conversation.ConversationNotFoundException
import kplich.backend.conversation.entities.Conversation
import kplich.backend.conversation.entities.Message
import kplich.backend.conversation.payloads.responses.conversation.SimpleConversationResponse
import kplich.backend.conversation.payloads.responses.message.SimpleMessageResponse
import kplich.backend.conversation.services.toResponse
import kplich.backend.items.entities.Item
import kplich.backend.items.services.toResponse
import kplich.backend.user.NoAcceptedOfferFound
import kplich.backend.user.NoConversationWithBuyerException
import kplich.backend.user.payloads.responses.items.ItemBoughtResponse
import kplich.backend.user.payloads.responses.items.ItemSoldResponse
import kplich.backend.user.payloads.responses.items.ItemWantedToBuyResponse
import kplich.backend.user.payloads.responses.items.ItemWantedToSellResponse

fun Conversation.toSimpleResponse(): SimpleConversationResponse {
    val lastMessage = this.messages.maxByOrNull { it.sentOn }!!
    val lastOffer = this.messages
            .filter { it.offer != null }
            .maxByOrNull { it.sentOn }
            ?.offer

    return SimpleConversationResponse(
            id = id,
            interestedUser = interestedUser.toSimpleResponse(),
            lastMessage = lastMessage.toSimpleResponse(),
            lastOffer = lastOffer?.toResponse()
    )
}

fun Message.toSimpleResponse(): SimpleMessageResponse {
    return SimpleMessageResponse(
            id = id,
            sendingUser = sender.toSimpleResponse(),
            sentOn = sentOn,
            textContent = content
    )
}

fun Item.toWantedToSellResponse(): ItemWantedToSellResponse {
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

fun Item.toSoldResponse(): ItemSoldResponse {
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

fun Item.toWantedToBuyResponse(interestedUser: ApplicationUser): ItemWantedToBuyResponse {
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

fun Item.toBoughtResponse(interestedUser: ApplicationUser): ItemBoughtResponse {
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