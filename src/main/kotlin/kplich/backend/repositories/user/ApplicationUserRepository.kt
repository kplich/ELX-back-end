package kplich.backend.repositories.user

import kplich.backend.entities.user.ApplicationUser
import org.springframework.data.jpa.repository.JpaRepository

interface ApplicationUserRepository : JpaRepository<ApplicationUser, Long> {
    fun findByUsername(username: String): ApplicationUser?

    fun existsByUsername(username: String): Boolean
}
