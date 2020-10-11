package kplich.backend.authentication.repositories

import kplich.backend.authentication.entities.ApplicationUser
import org.springframework.data.jpa.repository.JpaRepository

interface ApplicationUserRepository : JpaRepository<ApplicationUser, Long> {
    fun findByUsername(username: String): ApplicationUser?

    fun existsByUsername(username: String): Boolean
}
