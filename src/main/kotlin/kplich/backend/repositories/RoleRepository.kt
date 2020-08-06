package kplich.backend.repositories

import kplich.backend.entities.Role
import org.springframework.data.repository.CrudRepository

interface RoleRepository: CrudRepository<Role, Long> {
    fun findByName(name: Role.RoleEnum): Role?
}
