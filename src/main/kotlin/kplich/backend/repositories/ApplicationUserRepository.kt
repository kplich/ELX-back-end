package kplich.backend.repositories

import kplich.backend.entities.authentication.ApplicationUser
import org.springframework.data.jpa.repository.JpaRepository

interface ApplicationUserRepository : JpaRepository<ApplicationUser, Long> {
    fun findByUsername(username: String): ApplicationUser?

    fun existsByUsername(username: String): Boolean
}
