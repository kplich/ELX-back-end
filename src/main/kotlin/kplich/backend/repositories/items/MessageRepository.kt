package kplich.backend.repositories.items

import kplich.backend.entities.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository : JpaRepository<Message, Long>
