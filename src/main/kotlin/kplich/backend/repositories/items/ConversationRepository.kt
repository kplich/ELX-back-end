package kplich.backend.repositories.items

import kplich.backend.entities.authentication.ApplicationUser
import kplich.backend.entities.conversation.Conversation
import kplich.backend.entities.items.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ConversationRepository : JpaRepository<Conversation, Long> {
    fun findByInterestedUserAndItem(applicationUser: ApplicationUser, item: Item): Conversation?

    fun findAllByItemId(itemId: Long): List<Conversation>
}
