package kplich.backend.services.messages

import kplich.backend.configurations.security.WithMockIdUser
import kplich.backend.exceptions.items.IllegalConversationAccessException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MessageLoadingTest : MessageTest() {

    @Test
    @WithMockIdUser(id = 1, username = "kplich1")
    fun `user who added the item can see the conversation`() {
        val expectedConversation = conversations[1]
        val conversation = messageService.getConversation(4, 2)

        assertThat(conversation).isEqualTo(expectedConversation)
    }

    @Test
    @WithMockIdUser(id = 2, username = "kplich2")
    fun `user who responded to the item can see the conversation`() {
        val expectedConversation = conversations[1]
        val conversation = messageService.getConversation(4)

        assertThat(conversation).isEqualTo(expectedConversation)
    }

    @Test
    @WithMockIdUser(id = 3, username = "kplich3")
    fun `user who hasn't got anything to do with the conversation can't see it`() {

        assertThrows<IllegalConversationAccessException> {
            messageService.getConversation(4, 2)
        }
    }
}
