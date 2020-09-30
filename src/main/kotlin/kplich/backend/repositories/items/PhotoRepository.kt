package kplich.backend.repositories.items

import kplich.backend.entities.items.ItemPhoto
import org.springframework.data.jpa.repository.JpaRepository

interface PhotoRepository : JpaRepository<ItemPhoto, Long>
