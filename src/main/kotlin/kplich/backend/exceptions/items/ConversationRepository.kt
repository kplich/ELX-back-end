package kplich.backend.exceptions.items

import kplich.backend.entities.ApplicationUser
import kplich.backend.entities.Conversation
import kplich.backend.entities.Item
import org.springframework.data.repository.CrudRepository

interface ConversationRepository : CrudRepository<Conversation, Long> {
    fun findByInterestedUserAndItem(applicationUser: ApplicationUser, item: Item): Conversation?
}
