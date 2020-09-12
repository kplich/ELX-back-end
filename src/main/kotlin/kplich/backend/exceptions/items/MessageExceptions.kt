package kplich.backend.exceptions.items

import kplich.backend.exceptions.ElxResponseException
import org.springframework.http.HttpStatus

class IllegalConversationAccessException(violatingUserId: Long) :
        ElxResponseException(HttpStatus.UNAUTHORIZED,
                "User with id $violatingUserId cannot access this conversation!")

class NoUserIdProvidedException : ElxResponseException(HttpStatus.BAD_REQUEST, "User id is required for this request")

class ConversationNotFoundException(itemId: Long, subjectId: Long) :
        ElxResponseException(HttpStatus.NOT_FOUND, "No conversation about item $itemId with user $subjectId")

class ConversationWithSelfException :
        ElxResponseException(HttpStatus.BAD_REQUEST, "User cannot have a conversation with self")

class MessageToAClosedItemException(itemId: Long)
    : ElxResponseException(HttpStatus.CONFLICT, "Can't send a message to a closed item with id $itemId")

class OfferNotFoundException(offerId: Long)
    : ElxResponseException(HttpStatus.NOT_FOUND, "No offer found with id $offerId")

class UnauthorizedOfferModificationException(offerId: Long, loggedInId: Long)
    : ElxResponseException(HttpStatus.FORBIDDEN, "User with id $loggedInId cannot modify offer with id $offerId")

class OfferNotAwaitingAnswerException(offerId: Long)
    : ElxResponseException(HttpStatus.CONFLICT, "Offer with id $offerId has already been answered")
