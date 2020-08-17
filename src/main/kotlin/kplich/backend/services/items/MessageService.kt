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
    fun getConversation(itemId: Long, subjectId: Long?): ConversationResponse {
        val item = itemRepository.findByIdOrThrow(itemId, ::ItemNotFoundException)

        val currentlyLoggedId = UserService.getCurrentlyLoggedId()

        // conversation with whom?
        // depends who's asking - owner of the item or a possible buyer
        // if it's the owner, we need to know who are they talking to
        // if it's the buyer, we don't need to know anything more
        val conversationSubjectId: Long =
                if (item.addedBy.id == currentlyLoggedId)
                    subjectId ?: throw NoUserIdProvidedException()
                else currentlyLoggedId

        val conversationSubject = userRepository
                .findByIdOrThrow(conversationSubjectId, ::UserWithIdNotFoundException)

        return conversationRepository
                .findByInterestedUserAndItem(conversationSubject, item)
                ?.toResponse()
                ?: throw NoConversationFound(itemId, conversationSubjectId)
    }

    @Transactional
    @Throws(
            ItemNotFoundException::class,
            MessageToAClosedItemException::class,
            UserWithIdNotFoundException::class
    )
    fun sendMessage(itemId: Long, newMessageRequest: NewMessageRequest, subjectId: Long?): ConversationResponse {
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
                if (item.addedBy.id == currentlyLoggedId)
                    subjectId ?: throw NoUserIdProvidedException()
                else currentlyLoggedId

        val subject = userRepository
                .findByIdOrThrow(subjectIdNN, ::UserWithIdNotFoundException)

        val conversation = conversationRepository
                .findByInterestedUserAndItem(subject, item)
                ?: Conversation(subject, item, mutableListOf())

        val newMessage = messageRepository.save(newMessageRequest.mapToMessage(conversation, sender))
        conversation.messages.add(newMessage)

        val savedConversation = conversationRepository.save(conversation)
        return savedConversation.toResponse()
    }

    @Transactional
    fun declineOffer(offerId: Long) {
        val offer = offerRepository.findByIdOrThrow(offerId, ::NoOfferFoundException)

        if (offer.offerStatus != OfferStatus.AWAITING) {
            throw OfferNotAwaitingAnswerException(offerId)
        }

        offerRepository.save(offer.apply { this.offerStatus = OfferStatus.DECLINED })
    }

    @Transactional
    fun acceptOffer(offerId: Long, request: AcceptOfferRequest) {
        val acceptedOffer = offerRepository.findByIdOrThrow(offerId, ::NoOfferFoundException)

        if (acceptedOffer.offerStatus != OfferStatus.AWAITING) {
            throw OfferNotAwaitingAnswerException(offerId)
        }

        conversationRepository.findAllByItemId(acceptedOffer.message.conversation.item.id)
                .stream()
                .flatMap { conversation -> conversation.messages.stream() }
                .filter { message -> message.offer != null }
                .map { message -> message.offer }
                .filter { offer -> offer!!.id != acceptedOffer.id && offer.offerStatus != OfferStatus.AWAITING }
                .forEach { offer ->
                    offerRepository.save(offer!!.apply { offer.offerStatus = OfferStatus.DECLINED })
                }

        offerRepository.save(acceptedOffer.apply {
            offerStatus = OfferStatus.ACCEPTED
            contractAddress = request.contractAddress
        })
    }


    private fun Conversation.toResponse() = ResponseConverter.conversationToResponse(this)

    private fun NewMessageRequest.mapToMessage(conversation: Conversation, sender: ApplicationUser): Message {
        val message = Message(
                conversation = conversation,
                sender = sender,
                sentOn = LocalDateTime.now(),
                content = content
        )

        val offer = offer?.mapToOffer(message)

        return message.apply {
            this.offer = offer
        }
    }

    private fun NewOfferRequest.mapToOffer(message: Message): Offer = Offer(message, type, price, advance)
}
