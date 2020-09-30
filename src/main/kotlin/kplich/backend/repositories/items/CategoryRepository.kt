package kplich.backend.repositories.items

import kplich.backend.entities.items.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Int>
