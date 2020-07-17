package kplich.backend.exceptions

class UserAlreadyExistsException(username: String): ElxException("User with username $username already exists!")
