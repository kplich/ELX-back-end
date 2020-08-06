package kplich.backend.repositories.items

import kplich.backend.entities.Category
import org.springframework.data.repository.CrudRepository

interface CategoryRepository : CrudRepository<Category, Int>
