package kplich.backend.repositories.conversation

import kplich.backend.entities.conversation.Conversation
import kplich.backend.entities.user.ApplicationUser
import org.springframework.data.jpa.repository.JpaRepository

interface ConversationRepository : JpaRepository<Conversation, Long> {
    fun findAllByInterestedUser(applicationUser: ApplicationUser): List<Conversation>

    fun findAllByItemId(itemId: Long): List<Conversation>
}
