package kplich.backend.repositories

import kplich.backend.entities.ItemPhoto
import org.springframework.data.jpa.repository.JpaRepository

interface PhotoRepository: JpaRepository<ItemPhoto, Long>
