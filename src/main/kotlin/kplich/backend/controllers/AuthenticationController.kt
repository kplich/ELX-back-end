package kplich.backend.controllers

import kplich.backend.configurations.security.JwtUtil
import kplich.backend.configurations.security.UserDetailsImpl
import kplich.backend.entities.Role
import kplich.backend.entities.User
import kplich.backend.payloads.requests.SignupRequest
import kplich.backend.payloads.responses.JwtResponse
import kplich.backend.payloads.responses.SimpleMessageResponse
import kplich.backend.repositories.RoleRepository
import kplich.backend.repositories.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import kotlin.NoSuchElementException

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
        signupRequest.roles.forEach {
            val roleEnum = Role.RoleEnum.valueOf(it)
            val role = roleRepository.findByName(roleEnum) ?: throw NoSuchElementException("Role $it not found!")
            roles.add(role)
        }

        if(roles.size == 0) {
            roles.add(roleRepository.findByName(Role.RoleEnum.ROLE_USER) ?: throw Exception("Role ROLE_USER not found!"))
        }

        user.roles = roles

        userRepository.saveAndFlush(user)

        return authenticateUser(signupRequest.username, signupRequest.password)
    }

    private fun authenticateUser(username: String, password: String): ResponseEntity<*> {
        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))

        SecurityContextHolder.getContext().authentication = authentication
        val jwtToken = jwtUtil.generateJwtToken(authentication)

        val userDetails = authentication.principal as UserDetailsImpl

        return ResponseEntity.ok(JwtResponse(jwtToken, userDetails.username))

    }
}
