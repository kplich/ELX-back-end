package kplich.backend.repositories.items

import kplich.backend.entities.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Int>
