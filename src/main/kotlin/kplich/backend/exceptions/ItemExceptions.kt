package kplich.backend.exceptions

import org.springframework.http.HttpStatus

abstract class BadAddItemRequestException(override val message: String) : ElxResponseException(HttpStatus.BAD_REQUEST, message)

class NewItemUserNotFoundException(id: Long): BadAddItemRequestException("No user with ID $id found.")

class NewItemCategoryNotFoundException(id: Int): BadAddItemRequestException("No category with ID $id found")

class ItemNotFoundException(id: Long) : ElxResponseException(HttpStatus.NOT_FOUND, "Item with ID $id not found.")

class ItemAlreadyClosedException(id: Long): ElxResponseException(HttpStatus.CONFLICT, "Item with ID $id is already closed.")

class UnauthorizedItemUpdateRequestException(id: Long): ElxResponseException(HttpStatus.UNAUTHORIZED, "This user isn't authorized to update item with ID $id.")

class ClosedItemUpdateException: ElxResponseException(HttpStatus.CONFLICT, "A closed item can't be updated.")
