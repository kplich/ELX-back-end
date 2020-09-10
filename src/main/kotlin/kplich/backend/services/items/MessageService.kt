package kplich.backend.services.items

import kplich.backend.entities.*
import kplich.backend.exceptions.items.*
import kplich.backend.payloads.requests.items.AcceptOfferRequest
import kplich.backend.payloads.requests.items.NewMessageRequest
import kplich.backend.payloads.requests.items.NewOfferRequest
import kplich.backend.payloads.responses.items.ConversationResponse
import kplich.backend.repositories.ApplicationUserRepository
import kplich.backend.repositories.findByIdOrThrow
import kplich.backend.repositories.items.ConversationRepository
import kplich.backend.repositories.items.ItemRepository
import kplich.backend.repositories.items.MessageRepository
import kplich.backend.repositories.items.OfferRepository
import kplich.backend.services.ResponseConverter
import kplich.backend.services.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class MessageService(
        val itemRepository: ItemRepository,
        val userRepository: ApplicationUserRepository,
        val conversationRepository: ConversationRepository,
        val messageRepository: MessageRepository,
        val offerRepository: OfferRepository
) {
    @Transactional
    fun getConversation(itemId: Long, subjectId: Long? = null): ConversationResponse {
        val item = itemRepository.findByIdOrThrow(itemId, ::ItemNotFoundException)

        val currentlyLoggedId = UserService.getCurrentlyLoggedId()

        // conversation with whom?
        // depends who's asking - owner of the item or a possible buyer
        val subjectIdNN: Long =
                // if it's the owner, we need to know who are they talking to
                if (item.addedBy.id == currentlyLoggedId)
                    subjectId ?: throw NoUserIdProvidedException()
                // if it's the buyer, we don't need to know anything more
                else {
                    // the buyer shouldn't ask for someone else's conversation !
                    if (subjectId != null) {
                        throw IllegalConversationAccessException(currentlyLoggedId)
                    } else {
                        currentlyLoggedId
                    }
                }

        return item.conversations.find { it.interestedUser.id == subjectIdNN }
                ?.toResponse()
                ?: throw NoConversationFoundException(itemId, subjectIdNN)
    }

    @Transactional
    @Throws(
            ItemNotFoundException::class,
            IllegalConversationAccessException::class,
            MessageToAClosedItemException::class,
            UserWithIdNotFoundException::class,
    )
    // TODO: item owner cannot start a conversation
    fun sendMessage(itemId: Long, newMessageRequest: NewMessageRequest, subjectId: Long? = null): ConversationResponse {
        val item = itemRepository.findByIdOrThrow(itemId, ::ItemNotFoundException)

        if (item.closed) {
            throw MessageToAClosedItemException(itemId)
        }

        val currentlyLoggedId = UserService.getCurrentlyLoggedId()
        val sender = userRepository.findByIdOrThrow(currentlyLoggedId, ::UserWithIdNotFoundException)

        // conversation with whom?
        // depends who's asking - owner of the item or a possible buyer
        // if it's the owner, we need to know who are they talking to
        // if it's the buyer, we don't need to know anything more
        val subjectIdNN =
                // if it's the owner, we need to know who are they talking to
                if (item.addedBy.id == currentlyLoggedId)
                    subjectId ?: throw NoUserIdProvidedException()
                // if it's the buyer, we don't need to know anything more
                else {
                    // the buyer shouldn't ask for someone else's conversation !
                    if (subjectId != null) {
                        throw IllegalConversationAccessException(currentlyLoggedId)
                    } else {
                        currentlyLoggedId
                    }
                }

        val subject = userRepository
                .findByIdOrThrow(subjectIdNN, ::UserWithIdNotFoundException)

        val conversation = item.conversations.find { it.interestedUser.id == subjectIdNN }
                ?: if(currentlyLoggedId == item.addedBy.id) {
                    throw NoConversationFoundException(itemId, subjectIdNN)
                } else {
                    conversationRepository.save(Conversation(subject, item, mutableListOf()))
                }

        conversation.messages.add(newMessageRequest.mapToMessage(conversation, sender))

        val savedConversation = conversationRepository.save(conversation)
        return savedConversation.toResponse()
    }

    @Transactional
    fun declineOffer(offerId: Long): ConversationResponse {
        val offerToDecline = offerRepository.findByIdOrThrow(offerId, ::NoOfferFoundException)

        val loggedInId = UserService.getCurrentlyLoggedId()
        val itemOwner = offerToDecline.item.addedBy
        val interestedUser = offerToDecline.conversation.interestedUser

        // if the same user who sent the offer
        // or someone not related to the conversation
        // wants to decline the offer,
        // throw an exception
        if((offerToDecline.sender.id == loggedInId)
                || (loggedInId != itemOwner.id && loggedInId != interestedUser.id)) {
            throw UnauthorizedOfferModificationException(offerId, loggedInId)
        }

        if (!offerToDecline.awaiting) {
            throw OfferNotAwaitingAnswerException(offerId)
        }

        offerRepository.save(offerToDecline.decline())
        return offerToDecline.conversation.toResponse()
    }

    @Transactional
    fun acceptOffer(offerId: Long, request: AcceptOfferRequest): ConversationResponse {
        val offerToAccept = offerRepository.findByIdOrThrow(offerId, ::NoOfferFoundException)

        val loggedInId = UserService.getCurrentlyLoggedId()
        val itemOwner = offerToAccept.item.addedBy
        val interestedUser = offerToAccept.conversation.interestedUser

        // if the same user who sent the offer
        // or someone not related to the conversation
        // wants to accept the offer,
        // throw an exception
        if((offerToAccept.sender.id == loggedInId)
                || (loggedInId != itemOwner.id && loggedInId != interestedUser.id)) {
            throw UnauthorizedOfferModificationException(offerId, loggedInId)
        }

        if (!offerToAccept.awaiting) {
            throw OfferNotAwaitingAnswerException(offerId)
        }

        // get all offers regarding the item
        // and decline them
        conversationRepository.findAllByItemId(offerToAccept.item.id)
                .stream()
                .flatMap { conversation -> conversation.messages.stream() }
                .filter { message -> message.offer != null }
                .map { message -> message.offer }
                .filter { offer -> offer!!.id != offerToAccept.id && offer.awaiting }
                .forEach { offer ->
                    offerRepository.save(offer!!.decline())
                }

        offerRepository.save(offerToAccept.accept(request.contractAddress))
        itemRepository.save(offerToAccept.item.close())

        return offerToAccept.conversation.toResponse()
    }

    @Transactional
    fun cancelOffer(offerId: Long): ConversationResponse {
        val offerToCancel = offerRepository.findByIdOrThrow(offerId, ::NoOfferFoundException)

        val loggedInId = UserService.getCurrentlyLoggedId()

        if(loggedInId != offerToCancel.sender.id) {
            throw UnauthorizedOfferModificationException(offerId, loggedInId)
        }

        if(!offerToCancel.awaiting) {
            throw OfferNotAwaitingAnswerException(offerId)
        }

        offerRepository.save(offerToCancel.cancel())

        return offerToCancel.conversation.toResponse()
    }

    private fun Conversation.toResponse() = ResponseConverter.conversationToResponse(this)

    private fun NewMessageRequest.mapToMessage(conversation: Conversation, sender: ApplicationUser): Message {
        val message = messageRepository.save(Message(
                conversation = conversation,
                sender = sender,
                sentOn = LocalDateTime.now(),
                content = content
        ))

        val offer = offer?.mapToOffer(message)

        message.offer = offer
        return message
    }

    private fun NewOfferRequest.mapToOffer(message: Message): Offer =
            offerRepository.save(Offer(message, type, price, advance))
}
