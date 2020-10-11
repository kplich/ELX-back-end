package kplich.backend.items.repositories

import kplich.backend.items.entities.Item
import kplich.backend.authentication.entities.ApplicationUser
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long> {
    fun findByAddedBy(addedBy: ApplicationUser): List<Item>
}

