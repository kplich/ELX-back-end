package kplich.backend.services

import kplich.backend.entities.authentication.ApplicationUser
import kplich.backend.entities.conversation.Conversation
import kplich.backend.entities.conversation.Message
import kplich.backend.entities.conversation.offer.DoubleAdvanceOffer
import kplich.backend.entities.conversation.offer.Offer
import kplich.backend.entities.conversation.offer.PlainAdvanceOffer
import kplich.backend.entities.items.Category
import kplich.backend.entities.items.Item
import kplich.backend.payloads.responses.authentication.SimpleUserResponse
import kplich.backend.payloads.responses.conversation.ConversationResponse
import kplich.backend.payloads.responses.conversation.MessageResponse
import kplich.backend.payloads.responses.conversation.offer.DoubleAdvanceOfferResponse
import kplich.backend.payloads.responses.conversation.offer.OfferResponse
import kplich.backend.payloads.responses.conversation.offer.PlainAdvanceOfferResponse
import kplich.backend.payloads.responses.items.*

object ResponseConverter {
    fun itemToResponse(item: Item): ItemResponse = with(item) {
        ItemResponse(
                id,
                title,
                description,
                price,
                userToSimpleResponse(addedBy),
                addedOn,
                categoryToResponse(this.category),
                usedStatus,
                photos.map { photo -> photo.url },
                closedOn
        )
    }

    fun categoryToResponse(category: Category): CategoryResponse = with(category) {
        CategoryResponse(this.id, this.name)
    }

    fun userToSimpleResponse(user: ApplicationUser): SimpleUserResponse = with(user) {
        SimpleUserResponse(this.id, this.ethereumAddress, this.username)
    }

    fun conversationToResponse(conversation: Conversation): ConversationResponse = with(conversation) {
        ConversationResponse(
                id,
                itemToResponse(item),
                userToSimpleResponse(interestedUser),
                messages.map { message -> messageToResponse(message) }
        )
    }

    private fun messageToResponse(message: Message): MessageResponse = with(message) {
        MessageResponse(
                id,
                userToSimpleResponse(sender),
                sentOn,
                content,
                offer?.let { offerToResponse(it) }
        )
    }

    private fun offerToResponse(offer: Offer): OfferResponse {
        return when(offer) {
            is PlainAdvanceOffer -> PlainAdvanceOfferResponse(
                    advance = offer.advance,
                    id = offer.id,
                    price = offer.price,
                    offerStatus = offer.offerStatus,
                    contractAddress = offer.contractAddress
            )
            is DoubleAdvanceOffer -> DoubleAdvanceOfferResponse(
                    id = offer.id,
                    price = offer.price,
                    offerStatus = offer.offerStatus,
                    contractAddress = offer.contractAddress
            )
            else -> throw IllegalArgumentException("Cannot turn into offer response")
        }
    }
}
