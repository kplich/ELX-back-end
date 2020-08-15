package kplich.backend.controllers.authentication

import kplich.backend.payloads.requests.authentication.PasswordChangeRequest
import kplich.backend.payloads.requests.authentication.SignUpRequest
import kplich.backend.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@CrossOrigin
@RequestMapping("/auth")
class AuthenticationController(private val userService: UserService) {

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerUser(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<Unit> {
        userService.saveNewUser(signUpRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(Unit)
    }

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    fun changePassword(@RequestBody @Valid passwordChangeRequest: PasswordChangeRequest): ResponseEntity<Unit> {
        userService.changePassword(passwordChangeRequest)
        return ResponseEntity.ok(Unit)
    }
}
