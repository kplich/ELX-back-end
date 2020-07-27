package kplich.backend.exceptions

import kplich.backend.entities.Role
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException(val username: String) : ElxException("No user with username $username found!")

@ResponseStatus(HttpStatus.NOT_FOUND)
class RoleNotFoundException(role: Role.RoleEnum) : ElxException("Role $role not found!")

@ResponseStatus(HttpStatus.CONFLICT)
class UserAlreadyExistsException(username: String) : ElxException("User with username $username already exists!")
