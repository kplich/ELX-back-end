package kplich.backend.services.items

import kplich.backend.entities.authentication.ApplicationUser
import kplich.backend.entities.conversation.Conversation
import kplich.backend.entities.conversation.Message
import kplich.backend.entities.conversation.Offer
import kplich.backend.exceptions.authentication.NoUserLoggedInException
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
    @Throws(
            ItemNotFoundException::class,
            NoUserLoggedInException::class,
            IllegalConversationAccessException::class,
            ConversationNotFoundException::class
    )
    fun getConversation(itemId: Long, subjectId: Long): ConversationResponse {
        val item = itemRepository.findByIdOrThrow(itemId, ::ItemNotFoundException)

        val loggedInId = UserService.getCurrentlyLoggedId()
                ?: throw NoUserLoggedInException()

        if (item.addedBy.id != loggedInId) {
            throw IllegalConversationAccessException(loggedInId)
        }

        if (subjectId == loggedInId) {
            throw ConversationWithSelfException()
        }

        return item.conversations.find { it.interestedUser.id == subjectId }
                ?.toResponse()
                ?: throw ConversationNotFoundException(itemId, subjectId)
    }

    @Transactional
    @Throws(
            ItemNotFoundException::class,
            NoUserLoggedInException::class,
            NoUserIdProvidedException::class,
            ConversationNotFoundException::class
    )
    fun getConversation(itemId: Long): ConversationResponse {
        val item = itemRepository.findByIdOrThrow(itemId, ::ItemNotFoundException)

        val loggedInId = UserService.getCurrentlyLoggedId()
                ?: throw NoUserLoggedInException()

        if (item.addedBy.id == loggedInId) {
            throw NoUserIdProvidedException()
        }

        return item.conversations.find { it.interestedUser.id == loggedInId }
                ?.toResponse()
                ?: throw ConversationNotFoundException(itemId, loggedInId)
    }

    @Transactional
    @Throws(
            ItemNotFoundException::class,
            MessageToAClosedItemException::class,
            NoUserLoggedInException::class,
            ConversationWithSelfException::class
    )
    fun sendMessage(itemId: Long, newMessageRequest: NewMessageRequest): ConversationResponse {
        val item = itemRepository.findByIdOrThrow(itemId, ::ItemNotFoundException)

        if (item.closed) {
            throw MessageToAClosedItemException(itemId)
        }

        val loggedInId = UserService.getCurrentlyLoggedId()
                ?: throw NoUserLoggedInException()
        if (item.addedBy.id == loggedInId) {
            throw ConversationWithSelfException() // item owners cannot use this method
        }

        val sender = userRepository.findByIdOrThrow(loggedInId, ::UserWithIdNotFoundException)

        // if there's no conversation yet, create a new one
        val conversation = item.conversations.find { it.interestedUser == sender }
                ?: conversationRepository.saveAndFlush(Conversation(sender, item, mutableListOf()))

        val message = newMessageRequest.mapToMessage(conversation, sender)

        if (conversation.messages.size == 0) { // HACK: not sure why is this necessary
            conversation.messages.add(message)
        }

        return conversation.toResponse()
    }

    @Transactional
    @Throws(
            ItemNotFoundException::class,
            MessageToAClosedItemException::class,
            NoUserLoggedInException::class,
            IllegalConversationAccessException::class,
            ConversationNotFoundException::class
    )
    fun sendMessage(itemId: Long, newMessageRequest: NewMessageRequest, subjectId: Long): ConversationResponse {
        val item = itemRepository.findByIdOrThrow(itemId, ::ItemNotFoundException)
        if (item.closed) {
            throw MessageToAClosedItemException(itemId)
        }

        val loggedInId = UserService.getCurrentlyLoggedId()
                ?: throw NoUserLoggedInException()
        if(item.addedBy.id != loggedInId) {
            throw IllegalConversationAccessException(loggedInId) // only item owner can send messages this way
        }

        val sender = item.addedBy
        val subject = userRepository.findByIdOrThrow(subjectId, ::UserWithIdNotFoundException)

        val conversation = item.conversations.find { it.interestedUser == subject }
                ?: throw ConversationNotFoundException(itemId, subjectId)

        newMessageRequest.mapToMessage(conversation, sender)

        return conversation.toResponse()
    }

    @Transactional
    @Throws(
            OfferNotFoundException::class,
            NoUserLoggedInException::class,
            UnauthorizedOfferModificationException::class,
            OfferNotAwaitingAnswerException::class
    )
    fun declineOffer(offerId: Long): ConversationResponse {
        val offerToDecline = offerRepository.findByIdOrThrow(offerId, ::OfferNotFoundException)

        val loggedInId = UserService.getCurrentlyLoggedId()
                ?: throw NoUserLoggedInException()
        val itemOwner = offerToDecline.item.addedBy
        val interestedUser = offerToDecline.conversation.interestedUser

        // if the same user who sent the offer or someone not related to the conversation
        // wants to decline the offer, throw an exception
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
    @Throws(
            OfferNotFoundException::class,
            NoUserLoggedInException::class,
            UnauthorizedOfferModificationException::class,
            OfferNotAwaitingAnswerException::class
    )
    fun acceptOffer(offerId: Long, request: AcceptOfferRequest): ConversationResponse {
        val offerToAccept = offerRepository.findByIdOrThrow(offerId, ::OfferNotFoundException)

        val loggedInId = UserService.getCurrentlyLoggedId()
                ?: throw NoUserLoggedInException()
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
    @Throws(
            OfferNotFoundException::class,
            NoUserLoggedInException::class,
            UnauthorizedOfferModificationException::class,
            OfferNotAwaitingAnswerException::class
    )
    fun cancelOffer(offerId: Long): ConversationResponse {
        val offerToCancel = offerRepository.findByIdOrThrow(offerId, ::OfferNotFoundException)

        val loggedInId = UserService.getCurrentlyLoggedId()
                ?: throw NoUserLoggedInException()

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
        val message = messageRepository.saveAndFlush(Message(
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
