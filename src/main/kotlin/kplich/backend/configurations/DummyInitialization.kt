package kplich.backend.configurations

import kplich.backend.entities.Dummy
import kplich.backend.entities.DummyChild
import kplich.backend.repositories.DummyChildRepository
import kplich.backend.repositories.DummyRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DummyInitialization {
    @Bean
    fun databaseInitializer(
            dummyRepository: DummyRepository,
            dummyChildRepository: DummyChildRepository) = ApplicationRunner {
        val dummy = dummyRepository.save(Dummy("dummy string", 42))

        dummyChildRepository.save(DummyChild("dummy child string", dummy))
    }
}
