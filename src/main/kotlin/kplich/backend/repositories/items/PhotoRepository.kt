package kplich.backend.repositories.items

import kplich.backend.entities.ItemPhoto
import org.springframework.data.jpa.repository.JpaRepository

interface PhotoRepository : JpaRepository<ItemPhoto, Long>
