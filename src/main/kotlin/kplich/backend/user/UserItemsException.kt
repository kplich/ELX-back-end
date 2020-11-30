package kplich.backend.user

import kplich.backend.core.ElxResponseException
import org.springframework.http.HttpStatus

class NoConversationWithBuyerException(itemId: Long) : ElxResponseException(HttpStatus.INTERNAL_SERVER_ERROR, "No conversation with buyer has been found for item with id $itemId.")

class NoAcceptedOfferFound(itemId: Long, conversationId: Long) : ElxResponseException(HttpStatus.INTERNAL_SERVER_ERROR, "No accepted offer has been found in conversation with id $conversationId in item with id $itemId")