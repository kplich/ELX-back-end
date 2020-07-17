package kplich.backend.controllers

import kplich.backend.exceptions.UserAlreadyExistsException
import kplich.backend.payloads.requests.LoginRequest
import kplich.backend.payloads.requests.SignupRequest
import kplich.backend.payloads.responses.SimpleMessageResponse
import kplich.backend.services.UserDetailsServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@CrossOrigin
@RequestMapping("/auth")
class AuthenticationController(
        private val userService: UserDetailsServiceImpl){

    @PostMapping("/sign-up")
    fun registerUser(@Valid @RequestBody signupRequest: SignupRequest): ResponseEntity<*> {
        return try {
            userService.save(signupRequest)
            ResponseEntity.status(HttpStatus.OK).build<Nothing>()
        }
        catch (e: UserAlreadyExistsException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(SimpleMessageResponse(e.message))
        }
        catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)
        }
    }

    @PostMapping("/log-in")
    fun logInUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
        return try {
            val jwtResponse = userService.authenticateUser(loginRequest)
            ResponseEntity.ok(jwtResponse)
        }
        catch(e: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        }
        catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
    }
}
