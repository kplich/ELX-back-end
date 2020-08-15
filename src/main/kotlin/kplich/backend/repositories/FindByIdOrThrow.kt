package kplich.backend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun <T, ID, E : Throwable> JpaRepository<T, ID>.findByIdOrThrow(id: ID, throwableSupplier: (ID) -> E): T {
    return this.findByIdOrNull(id) ?: throw throwableSupplier(id)
}
