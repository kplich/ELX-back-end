package kplich.backend.services.conversation

import kplich.backend.entities.user.ApplicationUser
import kplich.backend.entities.conversation.Conversation
import kplich.backend.entities.conversation.Message
import kplich.backend.entities.conversation.offer.DoubleAdvanceOffer
import kplich.backend.entities.conversation.offer.Offer
import kplich.backend.entities.conversation.offer.PlainAdvanceOffer
import kplich.backend.exceptions.*
import kplich.backend.exceptions.NoUserLoggedInException
import kplich.backend.payloads.requests.conversation.AcceptOfferRequest
import kplich.backend.payloads.requests.conversation.NewMessageRequest
import kplich.backend.payloads.requests.conversation.offer.NewDoubleAdvanceOfferRequest
import kplich.backend.payloads.requests.conversation.offer.NewOfferRequest
import kplich.backend.payloads.requests.conversation.offer.NewPlainAdvanceOfferRequest
import kplich.backend.payloads.responses.conversation.conversation.ConversationResponse
import kplich.backend.repositories.user.ApplicationUserRepository
import kplich.backend.repositories.findByIdOrThrow
import kplich.backend.repositories.conversation.ConversationRepository
import kplich.backend.repositories.items.ItemRepository
import kplich.backend.repositories.conversation.MessageRepository
import kplich.backend.repositories.conversation.OfferRepository
import kplich.backend.services.ResponseConverter
import kplich.backend.services.user.UserService
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
        val generateRandomString = content == null && offer != null

        if(generateRandomString) {
            /* HACK: when saving an offer without message, no message is saved first,
                and validation fails
            */
            val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            val randomString = (1..15)
                    .map { kotlin.random.Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("")

            val message = messageRepository.save(Message(
                    conversation = conversation,
                    sender = sender,
                    sentOn = LocalDateTime.now(),
                    content = randomString,
            ))

            val offer = offer?.mapToOffer(message)

            message.offer = offer
            message.content = null
            return message
        }
        else {
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
    }

    private fun NewOfferRequest.mapToOffer(message: Message): Offer =
            when(this) {
                is NewPlainAdvanceOfferRequest -> offerRepository.save(PlainAdvanceOffer(advance, message, price))
                is NewDoubleAdvanceOfferRequest -> offerRepository.save(DoubleAdvanceOffer(message, price))
                else -> throw IllegalArgumentException("Can't map to offer!")
            }
}
