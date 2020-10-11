package kplich.backend.controllers.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import kplich.backend.authentication.AuthenticationController
import kplich.backend.configurations.security.JwtUtil
import kplich.backend.authentication.payloads.requests.PasswordChangeRequest
import kplich.backend.authentication.services.UserService
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.StringContains.containsString
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(AuthenticationController::class, JwtUtil::class)
class ChangePasswordTest {

    @MockBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `equal passwords will return 400 Bad Request`() {
        val passwordChangeRequest = PasswordChangeRequest(OLD_PASSWORD, OLD_PASSWORD)
        given(userService.changePassword(passwordChangeRequest)).will {  }

        mockMvc.perform(
                post(CHANGE_PASSWORD_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordChangeRequest))
                        .header(AUTHORIZATION, getTokenHeader()))
                .andExpect(status().isBadRequest)
                .andExpect(content().string(containsString(PasswordChangeRequest.PASSWORDS_MUSTNT_MATCH)))
    }

    @Test
    fun `incorrect new password will return 400 Bad Request`() {
        val passwordChangeRequest = PasswordChangeRequest(OLD_PASSWORD, NEW_INCORRECT_PASSWORD)
        given(userService.changePassword(passwordChangeRequest)).will { }

        mockMvc.perform(
                post(CHANGE_PASSWORD_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordChangeRequest))
                        .header(AUTHORIZATION, getTokenHeader()))
                .andExpect(status().isBadRequest)
                .andExpect(content().string(containsString(PasswordChangeRequest.PASSWORD_MUST_HAVE_DIGIT)))
                .andExpect(content().string(containsString(PasswordChangeRequest.PASSWORD_MUST_HAVE_SPECIAL_CHARACTER)))
                .andExpect(content().string(containsString(PasswordChangeRequest.PASSWORD_MUST_HAVE_UPPERCASE_LETTER)))
                .andExpect(content().string(not(containsString(PasswordChangeRequest.PASSWORD_MUST_HAVE_LOWERCASE_LETTER))))
    }

    @Test
    fun `correct new password will return 200 OK`() {
        val passwordChangeRequest = PasswordChangeRequest(OLD_PASSWORD, NEW_CORRECT_PASSWORD)
        given(userService.changePassword(passwordChangeRequest)).will { }

        mockMvc.perform(
                post(CHANGE_PASSWORD_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordChangeRequest))
                        .header(AUTHORIZATION, getTokenHeader()))
                .andExpect(status().isOk)
                .andExpect(content().string(equalTo(EMPTY_JSON_BODY)))
    }

    @Test
    fun `request won't be serviced without authentication`() {
        val passwordChangeRequest = PasswordChangeRequest(OLD_PASSWORD, NEW_CORRECT_PASSWORD)
        given(userService.changePassword(passwordChangeRequest)).will { }

        mockMvc.perform(
                post(CHANGE_PASSWORD_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordChangeRequest)))
                .andExpect(status().isForbidden)
                .andExpect(content().string(equalTo(EMPTY_BODY)))
    }

    private fun getTokenHeader(): String {
        return "$BEARER ${jwtUtil.generateJwt(ID, USERNAME, ETHEREUM_ADDRESS)}"
    }

    companion object {
        private const val ID = 1000L
        private const val USERNAME = "testing_user"
        private const val ETHEREUM_ADDRESS = "0xc1912fee45d61c87cc5ea59dae31190fffff232d"

        private const val AUTHORIZATION = "Authorization"
        private const val BEARER = "Bearer"

        private const val CHANGE_PASSWORD_PATH = "/auth/change-password"

        private const val OLD_PASSWORD = "old_password"
        private const val NEW_INCORRECT_PASSWORD = "password"
        private const val NEW_CORRECT_PASSWORD = "P@ssw0rd"

        private const val EMPTY_BODY = ""
        private const val EMPTY_JSON_BODY = "{}"
    }
}
