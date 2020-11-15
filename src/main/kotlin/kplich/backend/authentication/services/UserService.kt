package kplich.backend.authentication.services

import kplich.backend.authentication.*
import kplich.backend.configurations.security.getAuthoritiesFromRoles
import kplich.backend.authentication.entities.ApplicationUser
import kplich.backend.authentication.entities.Role
import kplich.backend.authentication.payloads.requests.PasswordChangeRequest
import kplich.backend.authentication.payloads.requests.SetEthereumAddressRequest
import kplich.backend.authentication.payloads.requests.SignUpRequest
import kplich.backend.authentication.payloads.responses.SimpleUserResponse
import kplich.backend.authentication.repositories.ApplicationUserRepository
import kplich.backend.authentication.repositories.RoleRepository
import kplich.backend.core.findByIdOrThrow
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

// @Lazy() annotations required due to beans created in WebSecurity configuration that depends on UserDetailsService
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

    @Throws(UserWithIdNotFoundException::class)
    fun getUser(id: Long): ApplicationUser {
        return userRepository.findByIdOrThrow(id, ::UserWithIdNotFoundException)
    }

    @Throws(UserAlreadyExistsException::class, RoleNotFoundException::class)
    fun saveNewUser(signUpRequest: SignUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.username)) {
            throw UserAlreadyExistsException(signUpRequest.username)
        }

        if (signUpRequest.ethereumAddress != null) {
            if (userRepository.existsByEthereumAddress(signUpRequest.ethereumAddress)) {
                throw EthereumAddressAlreadyExistsException(signUpRequest.ethereumAddress)
            }
        }

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
        val user = getLoggedInUser()

        if(userRepository.existsByEthereumAddress(setEthereumAddressRequest.ethereumAddress))

        if(user.ethereumAddress != null) {
            throw EthereumAddressAlreadySetException(user.id)
        }

        user.ethereumAddress = setEthereumAddressRequest.ethereumAddress

        userRepository.save(user)
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

    @Throws(
            NoUserLoggedInException::class,
            UserWithIdNotFoundException::class
    )
    fun getLoggedInUser(): ApplicationUser {
        val loggedInId = getCurrentlyLoggedId()
                ?: throw NoUserLoggedInException()

        return userRepository.findByIdOrThrow(loggedInId, ::UserWithIdNotFoundException)
    }

    companion object {
        private fun getCurrentlyLoggedId(): Long? {
            return SecurityContextHolder.getContext()
                    ?.authentication
                    ?.details as? Long
        }
    }
}
