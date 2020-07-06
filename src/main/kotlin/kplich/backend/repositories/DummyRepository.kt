package kplich.backend.repositories

import kplich.backend.entities.Dummy
import org.springframework.data.repository.CrudRepository

interface DummyRepository : CrudRepository<Dummy, Long>
