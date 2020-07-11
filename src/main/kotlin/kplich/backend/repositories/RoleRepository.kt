package kplich.backend.repositories

import kplich.backend.entities.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository: JpaRepository<Role, Long> {
    fun findByName(name: Role.RoleEnum): Role?
}
