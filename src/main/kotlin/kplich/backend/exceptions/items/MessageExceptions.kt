package kplich.backend.exceptions.items

import kplich.backend.exceptions.ElxResponseException
import org.springframework.http.HttpStatus

class NoUserIdProvided : ElxResponseException(HttpStatus.BAD_REQUEST, "User id is required for this request")

class NoConversationFound(itemId: Long, userId: Long) :
        ElxResponseException(HttpStatus.NOT_FOUND, "No conversation about item $itemId with user $userId")
