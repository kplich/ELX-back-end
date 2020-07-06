package kplich.backend.repositories

import kplich.backend.entities.Dummy
import kplich.backend.entities.DummyChild
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
class DummyRepositoriesTest @Autowired constructor(
        val entityManager: TestEntityManager,
        val dummyRepository: DummyRepository,
        val dummyChildRepository: DummyChildRepository
) {

    @Test
    fun `When you add an entity, you can then retrieve it from the repository`() {
        val dummy = Dummy("dummy string", 42)
        entityManager.persist(dummy)

        val dummyChild = DummyChild("dummy child string", dummy)
        entityManager.persist(dummyChild)

        entityManager.flush()

        val foundDummyChild = dummyChildRepository.findByIdOrNull(dummyChild.id!!)
        assertThat(foundDummyChild).isEqualTo(dummyChild)
    }
}
