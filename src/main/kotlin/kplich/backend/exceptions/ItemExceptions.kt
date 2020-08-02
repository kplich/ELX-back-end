package kplich.backend.exceptions

import org.springframework.http.HttpStatus

abstract class BadEditItemRequestException(override val message: String) : ElxResponseException(HttpStatus.BAD_REQUEST, message)

class UserIdNotFoundException(id: Long) : BadEditItemRequestException("No user with ID $id found.")

class CategoryNotFoundException(id: Int) : BadEditItemRequestException("No category with ID $id found")

class ItemNotFoundException(id: Long) : ElxResponseException(HttpStatus.NOT_FOUND, "Item with ID $id not found.")

class ItemAlreadyClosedException(id: Long) : ElxResponseException(HttpStatus.CONFLICT, "Item with ID $id is already closed.")

class UnauthorizedItemAddingRequestException() : ElxResponseException(HttpStatus.UNAUTHORIZED, "User isn't authorized to add an item")

class UnauthorizedItemUpdateRequestException(id: Long) : ElxResponseException(HttpStatus.UNAUTHORIZED, "This user isn't authorized to update item with ID $id.")

class ClosedItemUpdateException : ElxResponseException(HttpStatus.CONFLICT, "A closed item can't be updated.")
