package kplich.backend.controllers

import kplich.backend.entities.Dummy
import kplich.backend.entities.DummyChild
import kplich.backend.repositories.DummyChildRepository
import kplich.backend.repositories.DummyRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dummy")
class DummyRestController(
        private val dummyRepository: DummyRepository,
        private val dummyChildRepository: DummyChildRepository) {
    @GetMapping("/all")
    fun findAllDummies(): MutableIterable<Dummy> = dummyRepository.findAll()

    @GetMapping("/children")
    fun findAllChildren(): MutableIterable<DummyChild> = dummyChildRepository.findAll()

    //this comment doesn't do anything
}
