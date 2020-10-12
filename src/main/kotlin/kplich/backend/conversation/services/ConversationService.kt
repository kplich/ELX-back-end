package kplich.backend.conversation.services

import kplich.backend.conversation.*
import kplich.backend.authentication.entities.ApplicationUser
import kplich.backend.conversation.entities.Conversation
import kplich.backend.conversation.entities.Message
import kplich.backend.conversation.entities.offer.DoubleAdvanceOffer
import kplich.backend.conversation.entities.offer.Offer
import kplich.backend.conversation.entities.offer.PlainAdvanceOffer
import kplich.backend.authentication.NoUserLoggedInException
import kplich.backend.authentication.UserWithIdNotFoundException
import kplich.backend.conversation.payloads.requests.AcceptOfferRequest
import kplich.backend.conversation.payloads.requests.NewMessageRequest
import kplich.backend.conversation.payloads.requests.offer.NewDoubleAdvanceOfferRequest
import kplich.backend.conversation.payloads.requests.offer.NewOfferRequest
import kplich.backend.conversation.payloads.requests.offer.NewPlainAdvanceOfferRequest
import kplich.backend.conversation.payloads.responses.conversation.FullConversationResponse
import kplich.backend.authentication.repositories.ApplicationUserRepository
import kplich.backend.core.findByIdOrThrow
import kplich.backend.conversation.repositories.ConversationRepository
import kplich.backend.items.repositories.ItemRepository
import kplich.backend.conversation.repositories.MessageRepository
import kplich.backend.conversation.repositories.OfferRepository
import kplich.backend.items.ItemNotFoundException
import kplich.backend.authentication.services.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ConversationService(
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
    fun getConversation(itemId: Long, subjectId: Long): FullConversationResponse {
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
                ?.toFullResponse()
                ?: throw ConversationNotFoundException(itemId, subjectId)
    }

    @Transactional
    @Throws(
            ItemNotFoundException::class,
            NoUserLoggedInException::class,
            NoUserIdProvidedException::class,
            ConversationNotFoundException::class
    )
    fun getConversation(itemId: Long): FullConversationResponse {
        val item = itemRepository.findByIdOrThrow(itemId, ::ItemNotFoundException)

        val loggedInId = UserService.getCurrentlyLoggedId()
                ?: throw NoUserLoggedInException()

        if (item.addedBy.id == loggedInId) {
            throw NoUserIdProvidedException()
        }

        return item.conversations.find { it.interestedUser.id == loggedInId }
                ?.toFullResponse()
                ?: throw ConversationNotFoundException(itemId, loggedInId)
    }

    @Transactional
    @Throws(
            ItemNotFoundException::class,
            MessageToAClosedItemException::class,
            NoUserLoggedInException::class,
            ConversationWithSelfException::class
    )
    fun sendMessage(itemId: Long, newMessageRequest: NewMessageRequest): FullConversationResponse {
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

        return conversation.toFullResponse()
    }

    @Transactional
    @Throws(
            ItemNotFoundException::class,
            MessageToAClosedItemException::class,
            NoUserLoggedInException::class,
            IllegalConversationAccessException::class,
            ConversationNotFoundException::class
    )
    fun sendMessage(itemId: Long, newMessageRequest: NewMessageRequest, subjectId: Long): FullConversationResponse {
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

        return conversation.toFullResponse()
    }

    @Transactional
    @Throws(
            OfferNotFoundException::class,
            NoUserLoggedInException::class,
            UnauthorizedOfferModificationException::class,
            OfferNotAwaitingAnswerException::class
    )
    fun declineOffer(offerId: Long): FullConversationResponse {
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

        if (!offerToDecline.isAwaiting) {
            throw OfferNotAwaitingAnswerException(offerId)
        }

        offerRepository.save(offerToDecline.decline())
        return offerToDecline.conversation.toFullResponse()
    }

    @Transactional
    @Throws(
            OfferNotFoundException::class,
            NoUserLoggedInException::class,
            UnauthorizedOfferModificationException::class,
            OfferNotAwaitingAnswerException::class
    )
    fun acceptOffer(offerId: Long, request: AcceptOfferRequest): FullConversationResponse {
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

        if (!offerToAccept.isAwaiting) {
            throw OfferNotAwaitingAnswerException(offerId)
        }

        // get all offers regarding the item
        // and decline them
        conversationRepository.findAllByItemId(offerToAccept.item.id)
                .stream()
                .flatMap { conversation -> conversation.messages.stream() }
                .filter { message -> message.offer != null }
                .map { message -> message.offer }
                .filter { offer -> offer!!.id != offerToAccept.id && offer.isAwaiting }
                .forEach { offer ->
                    offerRepository.save(offer!!.decline())
                }

        offerRepository.save(offerToAccept.accept(request.contractAddress))
        itemRepository.save(offerToAccept.item.close())

        return offerToAccept.conversation.toFullResponse()
    }

    @Transactional
    @Throws(
            OfferNotFoundException::class,
            NoUserLoggedInException::class,
            UnauthorizedOfferModificationException::class,
            OfferNotAwaitingAnswerException::class
    )
    fun cancelOffer(offerId: Long): FullConversationResponse {
        val offerToCancel = offerRepository.findByIdOrThrow(offerId, ::OfferNotFoundException)

        val loggedInId = UserService.getCurrentlyLoggedId()
                ?: throw NoUserLoggedInException()

        if(loggedInId != offerToCancel.sender.id) {
            throw UnauthorizedOfferModificationException(offerId, loggedInId)
        }

        if(!offerToCancel.isAwaiting) {
            throw OfferNotAwaitingAnswerException(offerId)
        }

        offerRepository.save(offerToCancel.cancel())

        return offerToCancel.conversation.toFullResponse()
    }

    private fun NewMessageRequest.mapToMessage(conversation: Conversation, sender: ApplicationUser): Message {
        val generateRandomString = content == null && offer != null

        if(generateRandomString) {
            /* HACK: when saving an offer without message, no message is saved first,
                and validation fails */
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
