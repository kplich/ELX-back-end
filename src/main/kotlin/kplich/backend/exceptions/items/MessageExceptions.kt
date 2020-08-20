package kplich.backend.exceptions.items

import kplich.backend.exceptions.ElxResponseException
import org.springframework.http.HttpStatus

class IllegalConversationAccessException(violatingUserId: Long) :
        ElxResponseException(HttpStatus.UNAUTHORIZED,
                "User with id $violatingUserId cannot access this conversation!")

class NoUserIdProvidedException : ElxResponseException(HttpStatus.BAD_REQUEST, "User id is required for this request")

class NoConversationFoundException(itemId: Long, userId: Long) :
        ElxResponseException(HttpStatus.NOT_FOUND, "No conversation about item $itemId with user $userId")

class MessageToAClosedItemException(itemId: Long)
    : ElxResponseException(HttpStatus.CONFLICT, "Can't send a message to a closed item with id $itemId")

class NoOfferFoundException(offerId: Long)
    : ElxResponseException(HttpStatus.NOT_FOUND, "No offer found with id $offerId")

class OfferNotAwaitingAnswerException(offerId: Long)
    : ElxResponseException(HttpStatus.CONFLICT, "Offer with id $offerId has already been answered")
