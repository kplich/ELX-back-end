package kplich.backend.authentication.services

import kplich.backend.authentication.entities.ApplicationUser
import kplich.backend.authentication.payloads.responses.SimpleUserResponse

fun ApplicationUser.toSimpleResponse(): SimpleUserResponse {
    return SimpleUserResponse(this.id, this.ethereumAddress, this.username)
}