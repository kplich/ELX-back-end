package kplich.backend.conversation.repositories

import kplich.backend.conversation.entities.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository : JpaRepository<Message, Long>
