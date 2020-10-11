package kplich.backend.services.conversations

import kplich.backend.configurations.security.WithMockIdUser
import kplich.backend.conversation.entities.offer.OfferStatus
import kplich.backend.conversation.ConversationWithSelfException
import kplich.backend.conversation.IllegalConversationAccessException
import kplich.backend.conversation.MessageToAClosedItemException
import kplich.backend.conversation.payloads.requests.NewMessageRequest
import kplich.backend.conversation.payloads.requests.offer.NewPlainAdvanceOfferRequest
import kplich.backend.conversation.payloads.responses.offer.PlainAdvanceOfferResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class ConversationMessageSendingTest: ConversationTest() {

    @Test
    @WithMockIdUser(id = 1, username = "kplich1")
    fun `item owner can send a message to the conversation`() {
        val messageContent = "New message from user 1"

        val messageRequest = NewMessageRequest(messageContent)

        val conversation = conversationService.sendMessage(4, messageRequest, 2)

        assertThat(conversation.messages.any {
            it.textContent == messageContent && it.sendingUser == users[1] ?: error("user not found")
        })
    }

    @Test
    @WithMockIdUser(id = 1, username = "kplich1")
    fun `item owner cannot send a message without providing interested user's ID`() {
        val messageContent = "New message from user 1"

        val messageRequest = NewMessageRequest(messageContent)

        assertThrows<ConversationWithSelfException> {
            conversationService.sendMessage(4, messageRequest)
        }
    }

    @Test
    @WithMockIdUser(id = 2, username = "kplich2")
    fun `interested user can send a message to the conversation`() {
        val messageContent = "New message from user 2"

        val messageRequest = NewMessageRequest(messageContent)

        val conversation = conversationService.sendMessage(4, messageRequest)

        assertThat(conversation.messages).anyMatch {
            it.textContent == messageContent && it.sendingUser == users[2] ?: error("user not found")
        }
    }

    @Test
    @WithMockIdUser(id = 3, username = "kplich3")
    fun `user not participating cannot send a message to the conversation`() {
        val messageContent = "New message from user 3"

        val messageRequest = NewMessageRequest(messageContent)

        assertThrows<IllegalConversationAccessException> {
            conversationService.sendMessage(4, messageRequest, 2)
        }
    }

    @Test
    @WithMockIdUser(id = 2, username = "kplich2")
    fun `interested user can send an offer to the conversation`() {
        val messageContent = "New message from user 2"

        val offerRequest = NewPlainAdvanceOfferRequest(
                price = BigDecimal("1.234"),
                advance = BigDecimal("0.123"))

        val messageRequest = NewMessageRequest(messageContent, offerRequest)

        val conversation = conversationService.sendMessage(4, messageRequest)

        val offers = conversation.messages.mapNotNull { message -> message.offer }
        assertThat(offers.size).isEqualTo(1)

        val offer = offers[0]
        assertThat(offer.offerStatus).isEqualTo(OfferStatus.AWAITING)
        assertThat(offer.price).isEqualTo(offerRequest.price)
        assertThat((offer as PlainAdvanceOfferResponse).advance).isEqualTo(offerRequest.advance)
    }

    @Test
    @WithMockIdUser(id = 2, username = "kplich2")
    fun `interested user cannot send a message to a closed item`() {
        assertThrows<MessageToAClosedItemException> {
            conversationService.sendMessage(8, NewMessageRequest("any content"))
        }
    }
}
