package kplich.backend.services

import kplich.backend.configurations.security.JwtUtil
import kplich.backend.configurations.security.getAuthoritiesFromRoles
import kplich.backend.configurations.security.getRolesAsStrings
import kplich.backend.entities.ApplicationUser
import kplich.backend.entities.Role
import kplich.backend.exceptions.RoleNotFoundException
import kplich.backend.exceptions.UserAlreadyExistsException
import kplich.backend.payloads.requests.LoginRequest
import kplich.backend.payloads.requests.PasswordChangeRequest
import kplich.backend.payloads.requests.SignUpRequest
import kplich.backend.payloads.responses.JwtResponse
import kplich.backend.repositories.ApplicationUserRepository
import kplich.backend.repositories.RoleRepository
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
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
        return User(user.username, user.password, getAuthoritiesFromRoles(user.roles))
    }

    @Throws(UserAlreadyExistsException::class, RoleNotFoundException::class)
    fun save(signUpRequest: SignUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.username)) {
            throw UserAlreadyExistsException(signUpRequest.username)
        }

        val user = ApplicationUser(signUpRequest.username, passwordEncoder.encode(signUpRequest.password))
        val roles = mutableSetOf<Role>()
        roles.add(roleRepository.findByName(Role.RoleEnum.ROLE_USER) ?: throw RoleNotFoundException(Role.RoleEnum.ROLE_USER))
        user.roles = roles

        userRepository.save(user)
    }

    fun authenticateUser(loginRequest: LoginRequest): JwtResponse {
        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))

        SecurityContextHolder.getContext().authentication = authentication
        val jwtToken = jwtUtil.generateJwt(loginRequest.username, SecurityContextHolder.getContext().authentication.getRolesAsStrings())

        val userDetails = authentication.principal as UserDetails

        return JwtResponse(jwtToken, userDetails.username)
    }

    @Throws(UsernameNotFoundException::class, RoleNotFoundException::class)

    fun changePassword(passwordChangeRequest: PasswordChangeRequest) {

        // it is assumed that user is authenticated and authorized to perform requests that will call this method,
        // therefore their credentials can be obtained
        val userAuthentication = SecurityContextHolder.getContext().authentication
        val username = userAuthentication.name

        val userDetails = loadUserByUsername(username)

        if(passwordChangeRequest.oldPassword != userDetails.password) {
            throw BadCredentialsException("Incorrect old password!")
        }

        val userWithNewPassword = ApplicationUser(username, passwordEncoder.encode(passwordChangeRequest.newPassword))
        val roles = mutableSetOf<Role>()
        roles.add(roleRepository.findByName(Role.RoleEnum.ROLE_USER)
                ?: throw RoleNotFoundException(Role.RoleEnum.ROLE_USER))
        userWithNewPassword.roles = roles

        userRepository.save(userWithNewPassword)
    }
}
