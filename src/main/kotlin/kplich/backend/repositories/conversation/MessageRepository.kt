package kplich.backend.repositories.conversation

import kplich.backend.entities.conversation.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository : JpaRepository<Message, Long>
