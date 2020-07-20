package kplich.backend.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import kplich.backend.configurations.security.JwtUtil
import kplich.backend.entities.Role
import kplich.backend.exceptions.RoleNotFoundException
import kplich.backend.exceptions.UserAlreadyExistsException
import kplich.backend.payloads.requests.SignUpRequest
import kplich.backend.services.UserDetailsServiceImpl
import org.hamcrest.core.StringContains.containsString
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.invocation.InvocationOnMock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Only most basic validations are tested.
 */
@WebMvcTest(AuthenticationController::class, JwtUtil::class)
class SignUpTest {

    @MockBean
    private lateinit var userService: UserDetailsServiceImpl

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `correct username and password return 201 Created`() {
        // given
        val signupRequest = SignUpRequest(CORRECT_USERNAME, CORRECT_PASSWORD)
        given(userService.saveNewUser(signupRequest)).will(mockUserServiceBehavior())

        // when
        mockMvc.perform(post(SIGN_UP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                // then
                .andExpect(status().isCreated)
    }

    @Test
    fun `too short username returns 400 Bad Request`() {
        // given
        val signupRequest = SignUpRequest(TOO_SHORT_USERNAME, CORRECT_PASSWORD)
        given(userService.saveNewUser(signupRequest)).will(mockUserServiceBehavior())

        // when
        mockMvc.perform(post(SIGN_UP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                // then
                .andExpect(status().isBadRequest)
                .andExpect(content().string(containsString(VALIDATION_ERROR)))
                .andExpect(content().string(containsString(SignUpRequest.USERNAME_MUST_BE_BETWEEN_3_AND_20)))
    }

    @Test
    fun `too short password returns 400 Bad Request`() {
        // given
        val signupRequest = SignUpRequest(CORRECT_USERNAME, TOO_SHORT_PASSWORD)
        given(userService.saveNewUser(signupRequest)).will(mockUserServiceBehavior())

        // when
        mockMvc.perform(post(SIGN_UP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                // then
                .andExpect(status().isBadRequest)
                .andExpect(content().string(containsString(VALIDATION_ERROR)))
                .andExpect(content().string(containsString(SignUpRequest.PASSWORD_MUST_BE_BETWEEN_8_AND_40)))
    }

    @Test
    fun `not matching password returns 400 Bad Request`() {
        // given
        val signupRequest = SignUpRequest(CORRECT_USERNAME, NOT_MATCHING_PASSWORD)
        given(userService.saveNewUser(signupRequest)).will(mockUserServiceBehavior())

        // when
        mockMvc.perform(post(SIGN_UP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                // then
                .andExpect(status().isBadRequest)
                .andExpect(content().string(containsString(VALIDATION_ERROR)))
                .andExpect(content().string(containsString(SignUpRequest.PASSWORD_MUST_HAVE_SPECIAL_CHARACTER)))
                .andExpect(content().string(containsString(SignUpRequest.PASSWORD_MUST_HAVE_DIGIT)))

    }

    @Test
    fun `empty username returns 400 Bad Request`() {
        // given
        val signupRequest = SignUpRequest(EMPTY_STRING, CORRECT_PASSWORD)
        given(userService.saveNewUser(signupRequest)).will(mockUserServiceBehavior())

        // when
        mockMvc.perform(post(SIGN_UP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                // then
                .andExpect(status().isBadRequest)
                .andExpect(content().string(containsString(VALIDATION_ERROR)))
                .andExpect(content().string(containsString(SignUpRequest.USERNAME_REQUIRED)))
    }

    @Test
    fun `empty password returns 400 Bad Request`() {
        // given
        val signupRequest = SignUpRequest(CORRECT_USERNAME, EMPTY_STRING)
        given(userService.saveNewUser(signupRequest)).will(mockUserServiceBehavior())

        // when
        mockMvc.perform(post(SIGN_UP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                // then
                .andExpect(status().isBadRequest)
                .andExpect(content().string(containsString(VALIDATION_ERROR)))
                .andExpect(content().string(containsString(SignUpRequest.PASSWORD_REQUIRED)))
    }

    @Test
    fun `existing user returns 409 Conflict`() {
        // given
        val signupRequest = SignUpRequest(EXISTING_USERNAME, CORRECT_PASSWORD)
        given(userService.saveNewUser(signupRequest)).will(mockUserServiceBehavior())

        // when
        mockMvc.perform(post(SIGN_UP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                // then
                .andExpect(status().isConflict)
                .andExpect(content().string(containsString(UserAlreadyExistsException(EXISTING_USERNAME).message)))
    }


    companion object {
        private const val SIGN_UP_PATH = "/auth/sign-up"
        private const val EMPTY_STRING = ""

        private const val CORRECT_USERNAME = "username"
        private const val TOO_SHORT_USERNAME = "us"
        private const val EXISTING_USERNAME = "existing"
        private const val NO_ROLE_USERNAME = "norole"

        private const val CORRECT_PASSWORD = "P@ssw0rd"
        private const val TOO_SHORT_PASSWORD = "pass"
        private const val NOT_MATCHING_PASSWORD = "password"

        private const val VALIDATION_ERROR = "Validation Error"

        private fun mockUserServiceBehavior(): (InvocationOnMock) -> Unit {
            return {
                val req: SignUpRequest = it.arguments[0] as SignUpRequest
                if (req.username == EXISTING_USERNAME) {
                    throw UserAlreadyExistsException(req.username)
                }
                if (req.username == NO_ROLE_USERNAME) {
                    throw RoleNotFoundException(Role.RoleEnum.ROLE_USER)
                }
            }
        }
    }
}
