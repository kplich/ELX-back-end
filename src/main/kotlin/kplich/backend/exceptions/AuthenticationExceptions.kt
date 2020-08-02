package kplich.backend.exceptions

import kplich.backend.entities.Role
import org.springframework.http.HttpStatus

class RoleNotFoundException(role: Role.RoleEnum) : ElxResponseException(HttpStatus.NOT_FOUND, "Role $role not found!")

class UserAlreadyExistsException(username: String) : ElxResponseException(HttpStatus.CONFLICT, "User with username $username already exists!")

class NoUserLoggedInException : ElxResponseException(HttpStatus.UNAUTHORIZED, "No user is logged in!")
