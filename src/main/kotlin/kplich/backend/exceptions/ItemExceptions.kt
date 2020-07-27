package kplich.backend.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
abstract class BadAddItemRequestException(override val message: String) : ElxException(message)

class ItemAddingUserNotFound(id: Long): BadAddItemRequestException("No user with id $id found.")

class NewItemCategoryNotFound(id: Int): BadAddItemRequestException("No category with id $id found")

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class ItemNotFoundException(id: Long) : ElxException("Item with id $id not found.")

@ResponseStatus(value = HttpStatus.CONFLICT)
class ItemAlreadyClosedException(id: Long): ElxException("Item with id $id is already closed.")

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
class UnauthorizedItemUpdateRequestException(id: Long): ElxException("This user isn't authorized to update item with id $id.")

@ResponseStatus(value = HttpStatus.CONFLICT)
class ClosedItemUpdateException: ElxException("A closed item can't be updated.")
