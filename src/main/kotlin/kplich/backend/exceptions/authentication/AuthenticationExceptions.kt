package kplich.backend.exceptions.authentication

import kplich.backend.entities.authentication.Role
import kplich.backend.exceptions.ElxResponseException
import org.springframework.http.HttpStatus

class RoleNotFoundException(role: Role.RoleEnum) : ElxResponseException(HttpStatus.NOT_FOUND, "Role $role not found!")

class UserAlreadyExistsException(username: String) : ElxResponseException(HttpStatus.CONFLICT, "User with username $username already exists!")

class NoUserLoggedInException : ElxResponseException(HttpStatus.UNAUTHORIZED, "No user is logged in!")
