package kplich.backend.exceptions

class BadAddItemRequestException(override val message: String) : ElxException(message)

class ItemNotFoundException(id: Long) : ElxException("Item with id $id not found.")

class ItemAlreadyClosedException(id: Long): ElxException("Item with id $id is already closed.")

class UnauthorizedItemClosingRequest(username: String, id: Long): ElxException("Username $username isn't allowed to close item with id $id")
