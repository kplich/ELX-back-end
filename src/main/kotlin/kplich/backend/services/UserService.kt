package kplich.backend.services

import kplich.backend.configurations.security.getAuthoritiesFromRoles
import kplich.backend.entities.authentication.ApplicationUser
import kplich.backend.entities.authentication.Role
import kplich.backend.exceptions.authentication.*
import kplich.backend.payloads.requests.authentication.PasswordChangeRequest
import kplich.backend.payloads.requests.authentication.SetEthereumAddressRequest
import kplich.backend.payloads.requests.authentication.SignUpRequest
import kplich.backend.payloads.responses.authentication.SimpleUserResponse
import kplich.backend.repositories.ApplicationUserRepository
import kplich.backend.repositories.RoleRepository
import kplich.backend.repositories.findByIdOrThrow
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.BadCredentialsException
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
class UserService(
        private val userRepository: ApplicationUserRepository,
        private val roleRepository: RoleRepository,
        @Lazy private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    @Transactional(readOnly = true)
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val appUser = userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
        return User(appUser.username, appUser.password, getAuthoritiesFromRoles(appUser.roles))
    }

    @Throws(UsernameNotFoundException::class)
    fun getIdOfUsername(username: String): Long {
        return userRepository.findByUsername(username)?.id ?: throw UsernameNotFoundException(username)
    }

    fun getUser(id: Long): SimpleUserResponse {
        return userRepository.findByIdOrThrow(id, ::UserWithIdNotFoundException).toResponse()
    }

    @Throws(UserAlreadyExistsException::class, RoleNotFoundException::class)
    fun saveNewUser(signUpRequest: SignUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.username)) {
            throw UserAlreadyExistsException(signUpRequest.username)
        }

        println(passwordEncoder.hashCode())
        val user = ApplicationUser(signUpRequest.username, passwordEncoder.encode(signUpRequest.password), signUpRequest.ethereumAddress)
        val roles = mutableSetOf<Role>()
        roles.add(roleRepository.findByName(Role.RoleEnum.ROLE_USER) ?: throw RoleNotFoundException(Role.RoleEnum.ROLE_USER))
        user.roles = roles

        userRepository.save(user)
    }

    @Throws(
            NoUserLoggedInException::class,
            UserWithIdNotFoundException::class,
            EthereumAddressAlreadySetException::class)
    // TODO: test this
    fun setEthereumAddress(setEthereumAddressRequest: SetEthereumAddressRequest) {
        val loggedInId = getCurrentlyLoggedId() ?: throw NoUserLoggedInException()

        val user = userRepository.findByIdOrThrow(loggedInId, ::UserWithIdNotFoundException)

        if(user.ethereumAddress != null) {
            throw EthereumAddressAlreadySetException(loggedInId)
        }

        user.ethereumAddress = setEthereumAddressRequest.ethereumAddress
    }

    @Throws(RoleNotFoundException::class, AuthenticationCredentialsNotFoundException::class, BadCredentialsException::class)
    @Transactional
    fun changePassword(passwordChangeRequest: PasswordChangeRequest) {

        // it is assumed that user is authenticated and authorized to perform requests that will call this method,
        // therefore their credentials can be obtained
        val userAuthentication = SecurityContextHolder.getContext().authentication

        if (userAuthentication != null) {
            val username = userAuthentication.name

            val userDetails = loadUserByUsername(username)

            if (passwordEncoder.matches(passwordChangeRequest.oldPassword, userDetails.password)) {
                val userWithNewPassword = userRepository.findByUsername(username)
                        ?: throw UsernameNotFoundException("Username $username not found!")
                userWithNewPassword.password = passwordEncoder.encode(passwordChangeRequest.newPassword)
                userRepository.save(userWithNewPassword)
            } else throw BadCredentialsException("Invalid old password!")
        } else {
            throw AuthenticationCredentialsNotFoundException("No authentication found!")
        }
    }

    companion object {
        fun getCurrentlyLoggedId(): Long? {
            return SecurityContextHolder.getContext()
                    ?.authentication
                    ?.details as? Long
        }
    }

    private fun ApplicationUser.toResponse(): SimpleUserResponse = ResponseConverter.userToSimpleResponse(this)
}
