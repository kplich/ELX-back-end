package kplich.backend.services.conversations

import kplich.backend.configurations.security.WithMockIdUser
import kplich.backend.entities.conversation.offer.OfferStatus
import kplich.backend.exceptions.items.OfferNotAwaitingAnswerException
import kplich.backend.exceptions.items.UnauthorizedOfferModificationException
import kplich.backend.payloads.requests.conversation.AcceptOfferRequest
import kplich.backend.services.items.ItemService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ConversationOfferAcceptingTest : ConversationTest() {

    @Autowired
    lateinit var itemService: ItemService

    @Test
    @WithMockIdUser(id = 1, username = "kplich1")
    fun `item owner can accept offer`() {
        val contractAddres = "for example"
        messageService.acceptOffer(2, AcceptOfferRequest(contractAddres))

        val conversationWithUser2 = messageService.getConversation(7, 2)
        val conversationWithUser3 = messageService.getConversation(7, 3)

        val user2Offer = conversationWithUser2.messages.mapNotNull { it.offer }[0]
        val user3Offer = conversationWithUser3.messages.mapNotNull { it.offer }[0]

        assertThat(user2Offer.offerStatus).isEqualTo(OfferStatus.ACCEPTED)
        assertThat(user3Offer.offerStatus).isEqualTo(OfferStatus.DECLINED)

        val itemBought = itemService.getItem(7)

        assertThat(itemBought.closedOn != null).isTrue
    }

    @Test
    @WithMockIdUser(id = 2, username = "kplich2")
    fun `offer sender cannot accept offer`() {
        assertThrows<UnauthorizedOfferModificationException> {
            messageService.acceptOffer(2, AcceptOfferRequest("anything"))
        }
    }

    @Test
    @WithMockIdUser(id = 1, username = "kplich1")
    fun `offer receiver cannot accept a cancelled offer`() {
        assertThrows<OfferNotAwaitingAnswerException> {
            messageService.acceptOffer(4, AcceptOfferRequest("anything"))
        }
    }

}