package kplich.backend.services

import kplich.backend.configurations.security.JwtUtil
import kplich.backend.entities.Role
import kplich.backend.payloads.requests.LoginRequest
import kplich.backend.payloads.requests.SignupRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserDetailsServiceImplTest {
    @Autowired
    private lateinit var userService: UserDetailsServiceImpl

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Test
    @Order(1)
    fun `non-existing users throw exception`() {
        assertThrows<UsernameNotFoundException> { userService.loadUserByUsername(USERNAME) }
        assertThrows<UsernameNotFoundException> { userService.loadUserByUsername(USERNAME_2) }
        assertThrows<UsernameNotFoundException> { userService.loadUserByUsername(USERNAME_3) }
    }

    @Test
    @Order(2)
    fun `new user is added and returned`() {
        userService.save(SignupRequest(USERNAME, CORRECT_PASWORD))

        val user = userService.loadUserByUsername(USERNAME)
        assertThat(user).isNotNull
        assertThat(user.username).isEqualTo(USERNAME)
        assertThat(user.authorities).contains(userAuthority)
    }

    @Test
    @Order(3)
    fun `added user can be authenticated`() {
        val jwtResponse = userService.authenticateUser(LoginRequest(USERNAME, CORRECT_PASWORD))
        assertThat(jwtResponse).isNotNull
        assertThat(jwtResponse.username).isEqualTo(USERNAME)
        assertThat(jwtUtil.parseJwtClaims(jwtResponse.jwtToken)).isNotNull
    }

    @Test
    @Order(4)
    fun `wrong password can't be authenticated`() {
        assertThrows<BadCredentialsException> {
            userService.authenticateUser(LoginRequest(USERNAME, INCORRECT_PASSWORD))
        }
    }


    @Test
    @Order(5)
    fun `non-existent can't be authenticated`() {
        assertThrows<BadCredentialsException> {
            userService.authenticateUser(LoginRequest(USERNAME_2, CORRECT_PASWORD))
        }
    }

    companion object {
        private val userAuthority = SimpleGrantedAuthority(Role.RoleEnum.ROLE_USER.name)

        private const val USERNAME = "username"
        private const val USERNAME_2 = "kplich"
        private const val USERNAME_3 = "kplich_2"

        private const val CORRECT_PASWORD = "P@ssw0rd"
        private const val INCORRECT_PASSWORD = "password"
    }
}
