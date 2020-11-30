package kplich.backend.items.repositories

import kplich.backend.items.entities.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Int>
