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
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
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
        val appUser = userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
        return User(appUser.username, appUser.password, getAuthoritiesFromRoles(appUser.roles))
    }

    @Throws(UserAlreadyExistsException::class, RoleNotFoundException::class)
    fun saveNewUser(signUpRequest: SignUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.username)) {
            throw UserAlreadyExistsException(signUpRequest.username)
        }

        println(passwordEncoder.hashCode())
        val user = ApplicationUser(signUpRequest.username, passwordEncoder.encode(signUpRequest.password))
        val roles = mutableSetOf<Role>()
        roles.add(roleRepository.findByName(Role.RoleEnum.ROLE_USER) ?: throw RoleNotFoundException(Role.RoleEnum.ROLE_USER))
        user.roles = roles

        userRepository.save(user)
    }

    @Throws(AuthenticationException::class)
    fun authenticateUser(loginRequest: LoginRequest): JwtResponse {
        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))

        SecurityContextHolder.getContext().authentication = authentication
        val jwtToken = jwtUtil.generateJwt(loginRequest.username, SecurityContextHolder.getContext().authentication.getRolesAsStrings())

        val userDetails = authentication.principal as UserDetails

        return JwtResponse(jwtToken, userDetails.username)
    }

    @Throws(RoleNotFoundException::class, AuthenticationCredentialsNotFoundException::class, BadCredentialsException::class)
    @Transactional
    fun changePassword(passwordChangeRequest: PasswordChangeRequest) {

        // it is assumed that user is authenticated and authorized to perform requests that will call this method,
        // therefore their credentials can be obtained
        val userAuthentication = SecurityContextHolder.getContext().authentication

        if(userAuthentication != null) {
            val username = userAuthentication.name

            if(passwordEncoder.matches(passwordChangeRequest.oldPassword, userAuthentication.credentials as String)) {
                val userWithNewPassword = userRepository.findByUsername(username)
                        ?: throw UsernameNotFoundException("Username $username not found!")
                userWithNewPassword.password = passwordEncoder.encode(passwordChangeRequest.newPassword)
                userRepository.save(userWithNewPassword)
            }
            else throw BadCredentialsException("Invalid old password!")
        }
        else {
            throw AuthenticationCredentialsNotFoundException("No authentication found!")
        }
    }
}
