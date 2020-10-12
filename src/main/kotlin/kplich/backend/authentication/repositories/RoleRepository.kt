package kplich.backend.authentication.repositories

import kplich.backend.authentication.entities.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: Role.RoleEnum): Role?
}
