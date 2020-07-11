package kplich.backend.controllers

import kplich.backend.configurations.security.JwtUtil
import kplich.backend.configurations.security.getRoles
import kplich.backend.entities.Role
import kplich.backend.entities.User
import kplich.backend.payloads.requests.LoginRequest
import kplich.backend.payloads.requests.SignupRequest
import kplich.backend.payloads.responses.JwtResponse
import kplich.backend.payloads.responses.SimpleMessageResponse
import kplich.backend.repositories.RoleRepository
import kplich.backend.repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@CrossOrigin
@RequestMapping("/auth")
class AuthenticationController(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder,
        private val roleRepository: RoleRepository,
        private val authenticationManager: AuthenticationManager,
        private val jwtUtil: JwtUtil
){

    @PostMapping("/sign-up")
    fun registerUser(@Valid @RequestBody signupRequest: SignupRequest): ResponseEntity<*> {
        if(userRepository.existsByUsername(signupRequest.username)) {
            return ResponseEntity.badRequest().body(SimpleMessageResponse("User with given username already exists!"))
        }

        val user = User(signupRequest.username, passwordEncoder.encode(signupRequest.password))

        val roles = mutableSetOf<Role>()
        roles.add(roleRepository.findByName(Role.RoleEnum.ROLE_USER) ?: throw Exception("Role ROLE_USER not found!"))
        user.roles = roles

        return try {
            val jwt = jwtUtil.generateJwtToken(signupRequest.username, listOf(Role.RoleEnum.ROLE_USER.name))
            userRepository.saveAndFlush(user)
            ResponseEntity.ok(JwtResponse(jwt, signupRequest.username))
        }
        catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)
        }
    }

    @PostMapping("/log-in")
    fun logInUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
        return try {
            val jwtResponse = authenticateUser(loginRequest.username, loginRequest.password)
            ResponseEntity.ok(jwtResponse)
        } catch(e: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        }
        catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
    }

    private fun authenticateUser(username: String, password: String): JwtResponse {
        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))

        SecurityContextHolder.getContext().authentication = authentication
        val jwtToken = jwtUtil.generateJwtToken(username, getRoles(SecurityContextHolder.getContext().authentication))

        val userDetails = authentication.principal as UserDetails

        return JwtResponse(jwtToken, userDetails.username)
    }
}
