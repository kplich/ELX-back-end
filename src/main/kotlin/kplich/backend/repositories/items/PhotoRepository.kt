package kplich.backend.repositories.items

import kplich.backend.entities.ItemPhoto
import org.springframework.data.repository.CrudRepository

interface PhotoRepository: CrudRepository<ItemPhoto, Long>
