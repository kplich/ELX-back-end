package kplich.backend.repositories

import kplich.backend.entities.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository: JpaRepository<Category, Int> {
    fun findByName(name: String): Category?
}
