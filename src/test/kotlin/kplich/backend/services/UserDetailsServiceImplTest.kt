package kplich.backend.services

import kplich.backend.configurations.security.JwtUtil
import kplich.backend.entities.Role
import kplich.backend.exceptions.UserAlreadyExistsException
import kplich.backend.payloads.requests.LoginRequest
import kplich.backend.payloads.requests.PasswordChangeRequest
import kplich.backend.payloads.requests.SignUpRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.test.context.support.WithUserDetails

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserDetailsServiceImplTest {
    @Autowired
    private lateinit var userService: UserDetailsServiceImpl

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Test
    @Order(1)
    fun `non-existing users are not in the database and can't be authenticated`() {
        assertThrows<UsernameNotFoundException> {
            userService.loadUserByUsername(USERNAME)
        }

        assertThrows<AuthenticationException> {
            userService.authenticateUser(LoginRequest(USERNAME, ANY_PASSWORD))
        }

        assertThrows<UsernameNotFoundException> {
            userService.loadUserByUsername(UNAUTHENTICATED_USERNAME)
        }

        assertThrows<AuthenticationException> {
            userService.authenticateUser(LoginRequest(UNAUTHENTICATED_USERNAME, ANY_PASSWORD))
        }
    }

    @Test
    @Order(2)
    fun `new user is added and returned`() {
        userService.saveNewUser(SignUpRequest(USERNAME, CORRECT_PASWORD))

        val user = userService.loadUserByUsername(USERNAME)
        assertThat(user).isNotNull
        assertThat(user.username).isEqualTo(USERNAME)
        assertThat(user.authorities).contains(userAuthority)
    }

    @Test
    @Order(3)
    fun `new user with already existing username can't be added again`() {
        assertThrows<UserAlreadyExistsException> {
            userService.saveNewUser(SignUpRequest(USERNAME, CORRECT_PASWORD))
        }
    }

    @Test
    @Order(4)
    fun `added user can be authenticated`() {
        val jwtResponse = userService.authenticateUser(LoginRequest(USERNAME, CORRECT_PASWORD))
        assertThat(jwtResponse).isNotNull
        assertThat(jwtResponse.username).isEqualTo(USERNAME)
        assertThat(jwtUtil.parseJwtClaims(jwtResponse.jwtToken)).isNotNull
    }

    @Test
    @Order(5)
    fun `wrong password can't be authenticated`() {
        assertThrows<BadCredentialsException> {
            userService.authenticateUser(LoginRequest(USERNAME, INCORRECT_PASSWORD))
        }
    }

    @Test
    @Order(6)
    @WithUserDetails(USERNAME)
    fun `authenticated user can change password after providing correct old password`() {
        assertDoesNotThrow {
            userService.changePassword(PasswordChangeRequest(CORRECT_PASWORD, NEW_PASSWORD))
        }

        assertThrows<BadCredentialsException> {
            userService.authenticateUser(LoginRequest(USERNAME, CORRECT_PASWORD))
        }

        val jwtResponse = assertDoesNotThrow {
            userService.authenticateUser(LoginRequest(USERNAME, NEW_PASSWORD))
        }

        assertThat(jwtResponse).isNotNull
        assertThat(jwtResponse.username).isEqualTo(USERNAME)
        assertThat(jwtUtil.parseJwtClaims(jwtResponse.jwtToken)).isNotNull
    }

    @Test
    @Order(7)
    @WithUserDetails(USERNAME)
    fun `authenticated user can't change password after providing incorrect old password`() {
        assertThrows<BadCredentialsException> {
            userService.changePassword(PasswordChangeRequest(INCORRECT_PASSWORD, NEW_PASSWORD))
        }
    }

    companion object {
        private val userAuthority = SimpleGrantedAuthority(Role.RoleEnum.ROLE_USER.name)

        private const val USERNAME = "test_username"
        private const val UNAUTHENTICATED_USERNAME = "unauthenticated"

        private const val ANY_PASSWORD = "any"
        private const val CORRECT_PASWORD = "P@ssw0rd"
        private const val NEW_PASSWORD = "P@sswrrd2"
        private const val INCORRECT_PASSWORD = "password"
    }
}
