package kplich.backend.authentication

import io.swagger.annotations.*
import kplich.backend.authentication.payloads.requests.PasswordChangeRequest
import kplich.backend.authentication.payloads.requests.SetEthereumAddressRequest
import kplich.backend.authentication.payloads.requests.SignUpRequest
import kplich.backend.authentication.services.UserService
import kplich.backend.configurations.errorhandling.ApiError
import kplich.backend.conversation.payloads.responses.conversation.FullConversationResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
@Api(
        value = "Authentication controller",
        description = "Controller for managing authentication and user data."
)
@RestController
@CrossOrigin
@RequestMapping("/auth")
class AuthenticationController(private val userService: UserService) {

    @ApiOperation(value = "Register a new user")
    @ApiResponses(
            ApiResponse(code = 201, message = "Created the user", response = Any::class),
            ApiResponse(code = 404, message = "The user role does not exist!", response = ApiError::class),
            ApiResponse(code = 409, message = "User with such username or address already exists!", response = ApiError::class)
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-up", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun registerUser(
            @ApiParam("request for signing up")
            @Valid @RequestBody
            signUpRequest: SignUpRequest
    ): ResponseEntity<Any> {
        userService.saveNewUser(signUpRequest)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @ApiOperation(value = "Change the password")
    @ApiResponses(
            ApiResponse(code = 204, message = "Changed the password!"),
            ApiResponse(code = 401, message = "You're not authorized to change the password!"),
            ApiResponse(code = 403, message = "You're not allowed to access this endpoint!"),
            ApiResponse(code = 404, message = "The user role does not exist!")
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/change-password", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun changePassword(
            @ApiParam("request for changing the password")
            @Valid @RequestBody
            passwordChangeRequest: PasswordChangeRequest
    ): ResponseEntity<Any> {
        userService.changePassword(passwordChangeRequest)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @ApiOperation(value = "Change the password")
    @ApiResponses(
            ApiResponse(code = 204, message = "Set the Ethereum address!"),
            ApiResponse(code = 403, message = "You're not allowed to access this endpoint!"),
            ApiResponse(code = 404, message = "The user role does not exist!"),
            ApiResponse(code = 409, message = "User with such address already exists or the address is already set!")
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/set-ethereum-address", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun setEthereumAddress(
            @ApiParam("request for setting the Ethereum address")
            @Valid @RequestBody
            setEthereumAddressRequest: SetEthereumAddressRequest
    ): ResponseEntity<Any> {
        userService.setEthereumAddress(setEthereumAddressRequest)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
