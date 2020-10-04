package kplich.backend.repositories.authentication

import kplich.backend.entities.authentication.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: Role.RoleEnum): Role?
}
