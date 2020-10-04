package kplich.backend.repositories.items

import kplich.backend.entities.items.Item
import kplich.backend.entities.user.ApplicationUser
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long> {
    fun findByAddedBy(addedBy: ApplicationUser): List<Item>
}

