package kplich.backend

import kplich.backend.entities.Dummy
import kplich.backend.entities.DummyChild
import kplich.backend.repositories.DummyChildRepository
import kplich.backend.repositories.DummyRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DummyRestController(
        private val dummyRepository: DummyRepository,
        private val dummyChildRepository: DummyChildRepository) {
    @GetMapping("/dummy/all")
    fun findAllDummies(): MutableIterable<Dummy> = dummyRepository.findAll()

    @GetMapping("/child/all")
    fun findAllChildren(): MutableIterable<DummyChild> = dummyChildRepository.findAll()
}
