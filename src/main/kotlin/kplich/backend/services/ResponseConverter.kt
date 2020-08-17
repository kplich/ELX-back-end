package kplich.backend.services

import kplich.backend.entities.*
import kplich.backend.payloads.responses.authentication.SimpleUserResponse
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

    private fun userToSimpleResponse(user: ApplicationUser): SimpleUserResponse = with(user) {
        SimpleUserResponse(this.id, this.username)
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

    private fun offerToResponse(offer: Offer): OfferResponse = with(offer) {
        OfferResponse(
                id,
                type,
                price,
                advance,
                offerStatus,
                contractAddress
        )
    }
}
