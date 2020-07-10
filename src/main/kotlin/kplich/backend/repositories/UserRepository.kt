package kplich.backend.repositories

import kplich.backend.entities.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?

    fun existsByUsername(username: String): Boolean
}
