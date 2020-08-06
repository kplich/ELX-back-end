package kplich.backend.repositories

import kplich.backend.entities.ApplicationUser
import org.springframework.data.repository.CrudRepository

interface ApplicationUserRepository : CrudRepository<ApplicationUser, Long> {
    fun findByUsername(username: String): ApplicationUser?

    fun existsByUsername(username: String): Boolean
}
