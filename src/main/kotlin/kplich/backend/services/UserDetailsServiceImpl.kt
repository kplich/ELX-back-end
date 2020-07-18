package kplich.backend.services

import kplich.backend.configurations.security.JwtUtil
import kplich.backend.configurations.security.getRoles
import kplich.backend.entities.ApplicationUser
import kplich.backend.entities.Role
import kplich.backend.exceptions.RoleNotFoundException
import kplich.backend.exceptions.UserAlreadyExistsException
import kplich.backend.payloads.requests.LoginRequest
import kplich.backend.payloads.requests.SignupRequest
import kplich.backend.payloads.responses.JwtResponse
import kplich.backend.repositories.ApplicationUserRepository
import kplich.backend.repositories.RoleRepository
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

// Lazy annotations required due to beans created in WebSecurity configuration that depends on UserDetailsService

@Service
class UserDetailsServiceImpl(
        private val userRepository: ApplicationUserRepository,
        private val roleRepository: RoleRepository,
        private val jwtUtil: JwtUtil,
        @Lazy private val passwordEncoder: PasswordEncoder,
        @Lazy private val authenticationManager: AuthenticationManager
): UserDetailsService {

    @Transactional(readOnly = true)
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
        return User(user.username, user.password, emptyList())
    }

    @Throws(UserAlreadyExistsException::class, RoleNotFoundException::class)
    fun save(signupRequest: SignupRequest) {
        if(userRepository.existsByUsername(signupRequest.username)) {
            throw UserAlreadyExistsException(signupRequest.username)
        }

        val user = ApplicationUser(signupRequest.username, passwordEncoder.encode(signupRequest.password))

        val roles = mutableSetOf<Role>()
        roles.add(roleRepository.findByName(Role.RoleEnum.ROLE_USER) ?: throw RoleNotFoundException(Role.RoleEnum.ROLE_USER))
        user.roles = roles

        userRepository.save(user)
    }

    fun authenticateUser(loginRequest: LoginRequest): JwtResponse {
        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))

        SecurityContextHolder.getContext().authentication = authentication
        val jwtToken = jwtUtil.generateJwt(loginRequest.username, SecurityContextHolder.getContext().authentication.getRoles())

        val userDetails = authentication.principal as UserDetails

        return JwtResponse(jwtToken, userDetails.username)
    }
}
