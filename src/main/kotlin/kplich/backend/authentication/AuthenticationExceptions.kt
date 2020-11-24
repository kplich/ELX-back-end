package kplich.backend.authentication

import kplich.backend.core.ElxResponseException
import kplich.backend.authentication.entities.Role
import org.springframework.http.HttpStatus

class RoleNotFoundException(role: Role.RoleEnum) : ElxResponseException(HttpStatus.NOT_FOUND, "Role $role not found!")

class UserWithIdNotFoundException(id: Long): ElxResponseException(HttpStatus.NOT_FOUND, "User with id $id not found!")

class UserAlreadyExistsException(username: String) : ElxResponseException(HttpStatus.CONFLICT, "User with username $username already exists!")

class EthereumAddressAlreadyExistsException(ethereumAddress: String)
    : ElxResponseException(HttpStatus.CONFLICT, "User with address $ethereumAddress already exists!")

class EthereumAddressAlreadySetException(id: Long)
    : ElxResponseException(HttpStatus.CONFLICT, "User with id $id already has Ethereum address set!")

class NoUserLoggedInException : ElxResponseException(HttpStatus.FORBIDDEN, "No user is logged in!")
