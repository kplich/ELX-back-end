package kplich.backend.repositories.items

import kplich.backend.entities.Item
import org.springframework.data.repository.CrudRepository

interface ItemRepository: CrudRepository<Item, Long>

