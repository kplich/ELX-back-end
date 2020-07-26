package kplich.backend.exceptions

class UserNotFoundException(val username: String): ElxException("No user with username $username found!") {
}
