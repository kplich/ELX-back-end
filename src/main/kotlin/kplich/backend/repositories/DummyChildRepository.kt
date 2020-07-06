package kplich.backend.repositories

import kplich.backend.entities.DummyChild
import org.springframework.data.repository.CrudRepository

interface DummyChildRepository : CrudRepository<DummyChild, Long>
