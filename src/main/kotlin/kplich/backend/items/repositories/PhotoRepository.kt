package kplich.backend.items.repositories

import kplich.backend.items.entities.ItemPhoto
import org.springframework.data.jpa.repository.JpaRepository

interface PhotoRepository : JpaRepository<ItemPhoto, Long>
