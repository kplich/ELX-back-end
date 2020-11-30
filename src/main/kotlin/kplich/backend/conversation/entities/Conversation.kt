package kplich.backend.conversation.entities

import kplich.backend.authentication.entities.ApplicationUser
import kplich.backend.conversation.payloads.responses.conversation.FullConversationResponse
import kplich.backend.conversation.payloads.responses.conversation.SimpleConversationResponse
import kplich.backend.items.entities.Item
import javax.persistence.*

@Entity
@Table(
        name = "conversations",
        indexes = [Index(columnList = "interested_user_id, item_id", unique = true)]
)
data class Conversation(
        @OneToOne
        var interestedUser: ApplicationUser,

        @ManyToOne
        var item: Item,

        @OneToMany(mappedBy = "conversation")
        var messages: MutableList<Message>,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0
) {
    fun toSimpleResponse(): SimpleConversationResponse {
        val lastMessage = this.messages.maxByOrNull { it.sentOn }!!
        val lastOffer = this.messages
                .filter { it.offer != null }
                .maxByOrNull { it.sentOn }
                ?.offer

        return SimpleConversationResponse(
                id = id,
                interestedUser = interestedUser.toSimpleResponse(),
                lastMessage = lastMessage.toSimpleResponse(),
                lastOffer = lastOffer?.toResponse()
        )
    }

    fun toFullResponse(): FullConversationResponse {
        return FullConversationResponse(
                id = id,
                item = item.toResponse(),
                interestedUser = interestedUser.toSimpleResponse(),
                messages = messages.map { it.toResponse() }
        )
    }
}