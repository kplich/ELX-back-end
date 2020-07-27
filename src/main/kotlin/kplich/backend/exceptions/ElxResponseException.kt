package kplich.backend.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

abstract class ElxResponseException(status: HttpStatus, reason: String): ResponseStatusException(status, reason)
