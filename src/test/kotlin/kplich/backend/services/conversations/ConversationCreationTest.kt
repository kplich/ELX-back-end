package kplich.backend.services.conversations

import kplich.backend.configurations.security.WithMockIdUser
import kplich.backend.exceptions.items.NoConversationFoundException
import kplich.backend.payloads.requests.items.NewMessageRequest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
class ConversationCreationTest : ConversationTest() {
    @Test
    @WithMockIdUser(id = 2, username = "kplich2")
    fun `interested user can create a conversation`() {
        assertThrows<NoConversationFoundException> {
            messageService.getConversation(5)
        }

        val messageContent = "Really New message from user 2"

        val messageRequest = NewMessageRequest(messageContent)

        val conversation = messageService.sendMessage(5, messageRequest)

        Assertions.assertThat(conversation.messages.size).isEqualTo(1)
        Assertions.assertThat(conversation.messages[0].textContent).isEqualTo(messageContent)
        Assertions.assertThat(conversation.messages[0].sendingUser).isEqualTo(users[2] ?: error("user not found"))
    }

    @Test
    @WithMockIdUser(id = 1, username = "kplich1")
    fun `item owner cannot start a conversation`() {
        assertThrows<NoConversationFoundException> {
            messageService.getConversation(5, 2)
        }

        val messageContent = "Really New message from user 2"

        val messageRequest = NewMessageRequest(messageContent)

        assertThrows<NoConversationFoundException> {
            messageService.sendMessage(5, messageRequest, 2)
        }
    }
}