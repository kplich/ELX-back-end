package kplich.backend.conversation.repositories

import kplich.backend.conversation.entities.Conversation
import kplich.backend.authentication.entities.ApplicationUser
import org.springframework.data.jpa.repository.JpaRepository

interface ConversationRepository : JpaRepository<Conversation, Long> {
    fun findAllByInterestedUser(applicationUser: ApplicationUser): List<Conversation>

    fun findAllByItemId(itemId: Long): List<Conversation>
}
