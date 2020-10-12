package kplich.backend.core

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import javax.persistence.EntityNotFoundException

fun <T, ID, E : Throwable> JpaRepository<T, ID>.findByIdOrThrow(id: ID, throwableSupplier: (ID) -> E): T {
    return this.findByIdOrNull(id) ?: throw throwableSupplier(id)
}

fun <T, ID, E : Throwable> JpaRepository<T, ID>.getOneOrThrow(id: ID, throwableSupplier: (ID) -> E): T {
    return try {
        this.getOne(id)
    } catch (e: EntityNotFoundException) {
        throw throwableSupplier(id)
    }
}
