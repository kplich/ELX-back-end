package kplich.backend.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import kplich.backend.payloads.requests.LoginRequest
import kplich.backend.payloads.responses.JwtResponse
import kplich.backend.services.UserDetailsServiceImpl
import org.hamcrest.core.StringContains.containsString
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.invocation.InvocationOnMock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(AuthenticationController::class)
class LogInTest {

    @MockBean
    private lateinit var userService: UserDetailsServiceImpl

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `incorrect username returns 401 Unauthorized`() {
        // given
        val loginRequest = LoginRequest(INCORRECT_USERNAME, INCORRECT_PASSWORD)
        given(userService.authenticateUser(loginRequest)).will(mockUserServiceBehavior())

        // when
        mockMvc.perform(
                post(LOG_IN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                // then
                .andExpect(status().isUnauthorized)
    }

    @Test
    fun `incorrect password returns 401 Unauthorized`() {
        // given
        val loginRequest = LoginRequest(CORRECT_USERNAME, INCORRECT_PASSWORD)
        given(userService.authenticateUser(loginRequest)).will(mockUserServiceBehavior())

        // when
        mockMvc.perform(
                post(LOG_IN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                // then
                .andExpect(status().isUnauthorized)
    }

    @Test
    fun `correct username and password will return 200 OK and a response with JWT`() {
        // given
        val loginRequest = LoginRequest(CORRECT_USERNAME, CORRECT_PASSWORD)
        given(userService.authenticateUser(loginRequest)).will(mockUserServiceBehavior())

        // when
        mockMvc.perform(
                post(LOG_IN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                // then
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(JWT_RESPONSE)))
    }

    @Test
    fun `empty username will return 400 Bad Request`() {
        // given
        val loginRequest = LoginRequest(EMPTY_STRING, CORRECT_PASSWORD)
        given(userService.authenticateUser(loginRequest)).will(mockUserServiceBehavior())

        // when
        mockMvc.perform(
                post(LOG_IN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                // then
                .andExpect(status().isBadRequest)
                .andExpect(content().string(containsString(LoginRequest.USERNAME_REQUIRED)))
    }

    @Test
    fun `empty password will return 400 Bad Request`() {
        // given
        val loginRequest = LoginRequest(CORRECT_USERNAME, EMPTY_STRING)
        given(userService.authenticateUser(loginRequest)).will(mockUserServiceBehavior())

        // when
        mockMvc.perform(
                post(LOG_IN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                // then
                .andExpect(status().isBadRequest)
                .andExpect(content().string(containsString(LoginRequest.PASSWORD_REQUIRED)))
    }


    companion object {
        private const val LOG_IN_PATH = "/auth/log-in"
        private const val EMPTY_STRING = ""

        private const val CORRECT_USERNAME = "username"
        private const val INCORRECT_USERNAME = "user"

        private const val CORRECT_PASSWORD = "password"
        private const val INCORRECT_PASSWORD = "pass"

        private const val SAMPLE_JWT = "xxx.yyy.zzz"
        private val JWT_RESPONSE = JwtResponse(SAMPLE_JWT, CORRECT_USERNAME)

        private fun mockUserServiceBehavior(): (InvocationOnMock) -> JwtResponse {
            return {
                val req: LoginRequest = it.arguments[0] as LoginRequest
                if (req.username == CORRECT_USERNAME && req.password == CORRECT_PASSWORD) {
                    JWT_RESPONSE
                } else {
                    throw BadCredentialsException("")
                }
            }
        }
    }
}
