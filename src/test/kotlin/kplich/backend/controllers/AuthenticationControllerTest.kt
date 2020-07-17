package kplich.backend.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import kplich.backend.payloads.requests.LoginRequest
import kplich.backend.payloads.responses.JwtResponse
import kplich.backend.services.UserDetailsServiceImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import java.util.stream.Stream

@WebMvcTest(AuthenticationController::class)
class AuthenticationControllerTest {

    @MockBean
    private lateinit var userService: UserDetailsServiceImpl

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @ParameterizedTest
    @MethodSource("provideForTestLoginRequestStatus")
    fun testLoginRequestStatus(loginRequest: LoginRequest, expectedStatus: HttpStatus) {

        given(userService.authenticateUser(loginRequest)).will {
            val req: LoginRequest = it.arguments[0] as LoginRequest
            if (req.username == CORRECT_USERNAME && req.password == CORRECT_PASSWORD) {
                JwtResponse(SAMPLE_JWT, CORRECT_USERNAME)
            } else if (req.username == CORRECT_USERNAME && req.password != CORRECT_PASSWORD) {
                throw BadCredentialsException("Incorrect password!")
            } else {
                throw UsernameNotFoundException("No user with username ${req.username}!")
            }
        }

        mockMvc.perform(
                post("/auth/log-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(loginRequest)
                        )
        ).andExpect {
            assertThat(it.response.status).isEqualTo(expectedStatus.value())
        }
    }

    companion object {
        const val CORRECT_USERNAME = "username"
        private const val INCORRECT_USERNAME = "user"

        const val CORRECT_PASSWORD = "password"
        private const val INCORRECT_PASSWORD = "pass"

        const val SAMPLE_JWT = "xxx.yyy.zzz"

        @JvmStatic
        fun provideForTestLoginRequestStatus(): Stream<Arguments> {
            return Stream.of(
                    Arguments.of(LoginRequest(CORRECT_USERNAME, CORRECT_PASSWORD), HttpStatus.OK),
                    Arguments.of(LoginRequest(CORRECT_USERNAME, INCORRECT_PASSWORD), HttpStatus.UNAUTHORIZED),
                    Arguments.of(LoginRequest(INCORRECT_USERNAME, INCORRECT_PASSWORD), HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
    }
}
