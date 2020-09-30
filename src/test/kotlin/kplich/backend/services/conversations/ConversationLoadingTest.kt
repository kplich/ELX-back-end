package kplich.backend.services.conversations

import kplich.backend.configurations.security.WithMockIdUser
import kplich.backend.exceptions.items.IllegalConversationAccessException
import kplich.backend.exceptions.items.ConversationNotFoundException
import kplich.backend.exceptions.items.NoUserIdProvidedException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ConversationLoadingTest : ConversationTest() {

    @Test
    @WithMockIdUser(id = 1, username = "kplich1")
    fun `user who added the item can see the conversation`() {
        val expectedConversation = conversations[1]
        val conversation = messageService.getConversation(4, 2)

        assertThat(conversation).isEqualTo(expectedConversation)
    }

    @Test
    @WithMockIdUser(id = 1, username = "kplich1")
    fun `user who added the item must give interested user's ID`() {
        assertThrows<NoUserIdProvidedException> {
            messageService.getConversation(4)
        }
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
    fun `user not taking part in the conversation can't see it`() {
        assertThrows<IllegalConversationAccessException> {
            messageService.getConversation(4, 2)
        }
    }

    @Test
    @WithMockIdUser(id = 2, username = "kplich2")
    fun `interested user cannot load a conversation he hasn't started`() {
        assertThrows<ConversationNotFoundException> {
            messageService.getConversation(5)
        }
    }

    @Test
    @WithMockIdUser(id = 1, username = "kplich1")
    fun `item owner cannot load a conversation that hasn't been started`() {
        assertThrows<ConversationNotFoundException> {
            messageService.getConversation(5, 2)
        }
    }
}
