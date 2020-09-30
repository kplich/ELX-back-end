package kplich.backend.repositories.items

import kplich.backend.entities.items.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long>

