package kplich.backend.services.conversations

import kplich.backend.configurations.security.WithMockIdUser
import kplich.backend.entities.conversation.offer.OfferStatus
import kplich.backend.exceptions.OfferNotAwaitingAnswerException
import kplich.backend.exceptions.UnauthorizedOfferModificationException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ConversationOfferDecliningTest : ConversationTest() {

    @Test
    @WithMockIdUser(id = 2, username = "kplich2")
    fun `offer sender can cancel it`() {
        messageService.cancelOffer(1)

        val conversation = messageService.getConversation(6)

        val foundOffers = conversation.messages.mapNotNull { message -> message.offer }
        assertThat(foundOffers.size).isEqualTo(1)

        val foundOffer = foundOffers[0]
        assertThat(foundOffer.offerStatus).isEqualTo(OfferStatus.CANCELLED)
    }

    @Test
    @WithMockIdUser(id = 1, username = "kplich1")
    fun `offer receiver cannot cancel it`() {
        assertThrows<UnauthorizedOfferModificationException> {
            messageService.cancelOffer(1)
        }
    }

    @Test
    @WithMockIdUser(id = 2, username = "kplich2")
    fun `offer sender cannot decline it`() {
        assertThrows<UnauthorizedOfferModificationException> {
            messageService.declineOffer(1)
        }
    }

    @Test
    @WithMockIdUser(id = 1, username = "kplich1")
    fun `offer receiver can decline it`() {
        messageService.declineOffer(1)

        val conversation = messageService.getConversation(6, 2)

        val foundOffers = conversation.messages.mapNotNull { message -> message.offer }
        assertThat(foundOffers.size).isEqualTo(1)

        val foundOffer = foundOffers[0]
        assertThat(foundOffer.offerStatus).isEqualTo(OfferStatus.DECLINED)
    }

    @Test
    @WithMockIdUser(id = 3, username = "kplich3")
    fun `user not taking part in a conversation cannot cancel offer`() {
        assertThrows<UnauthorizedOfferModificationException> {
            messageService.cancelOffer(1)
        }
    }

    @Test
    @WithMockIdUser(id = 3, username = "kplich3")
    fun `user not taking part in a conversation cannot decline offer`() {
        assertThrows<UnauthorizedOfferModificationException> {
            messageService.declineOffer(1)
        }
    }

    @Test
    @WithMockIdUser(id = 2, username = "kplich2")
    fun `offer sender cannot cancel a rejected offer`() {
        assertThrows<OfferNotAwaitingAnswerException> {
            messageService.cancelOffer(5)
        }
    }
}