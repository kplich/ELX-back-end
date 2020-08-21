package kplich.backend.services.messages

import kplich.backend.configurations.security.WithMockIdUser
import kplich.backend.exceptions.items.IllegalConversationAccessException
import kplich.backend.exceptions.items.NoConversationFoundException
import kplich.backend.payloads.requests.items.NewMessageRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MessageSendingTest: MessageTest() {

    @Test
    @WithMockIdUser(id = 1, username = "kplich1")
    fun `item owner can send a message to the conversation`() {
        val messageContent = "New message from user 1"

        val messageRequest = NewMessageRequest(messageContent)

        val conversation = messageService.sendMessage(4, messageRequest, 2)

        assertThat(conversation.messages.any {
            it.textContent == messageContent && it.sendingUser == users[1] ?: error("user not found")
        })
    }

    @Test
    @WithMockIdUser(id = 2, username = "kplich2")
    fun `interested user can send a message to the conversation`() {
        val messageContent = "New message from user 2"

        val messageRequest = NewMessageRequest(messageContent)

        val conversation = messageService.sendMessage(4, messageRequest)

        assertThat(conversation.messages.any {
            it.textContent == messageContent && it.sendingUser == users[1] ?: error("user not found")
        })
    }

    @Test
    @WithMockIdUser(id = 3, username = "kplich3")
    fun `user not participating cannot send a message to the conversation`() {
        val messageContent = "New message from user 3"

        val messageRequest = NewMessageRequest(messageContent)

        assertThrows<IllegalConversationAccessException> {
            messageService.sendMessage(4, messageRequest, 2)
        }
    }

    @Test
    @WithMockIdUser(id = 2, username = "kplich2")
    fun `first message about item creates a new conversation`() {
        assertThrows<NoConversationFoundException> {
            messageService.getConversation(5)
        }

        val messageContent = "Really New message from user 2"

        val messageRequest = NewMessageRequest(messageContent)

        val conversation = messageService.sendMessage(5, messageRequest)

        assertThat(conversation.messages.size == 1)
        assertThat(conversation.messages[0].textContent == messageContent)
        assertThat(conversation.messages[0].sendingUser == users[2] ?: error("user not found"))
    }

}