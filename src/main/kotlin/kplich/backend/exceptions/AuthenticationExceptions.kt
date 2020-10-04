package kplich.backend.exceptions

import kplich.backend.entities.authentication.Role
import org.springframework.http.HttpStatus

class RoleNotFoundException(role: Role.RoleEnum) : ElxResponseException(HttpStatus.NOT_FOUND, "Role $role not found!")

class UserWithIdNotFoundException(id: Long): ElxResponseException(HttpStatus.NOT_FOUND, "User with id $id not found!")

class UserAlreadyExistsException(username: String) : ElxResponseException(HttpStatus.CONFLICT, "User with username $username already exists!")

class EthereumAddressAlreadySetException(id: Long)
    : ElxResponseException(HttpStatus.CONFLICT, "User with id $id already has Ethereum address set!")

class NoUserLoggedInException : ElxResponseException(HttpStatus.UNAUTHORIZED, "No user is logged in!")
