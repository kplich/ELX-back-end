package kplich.backend.configurations.errorhandling

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

data class ApiError(
        @JsonIgnore
        val httpStatus: HttpStatus,
        val message: String,
        val userMessage: String,
        val debugMessage: String,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        val timestamp: LocalDateTime = LocalDateTime.now()) {

    @get:JsonIgnore
    val response get(): ResponseEntity<Any> = ResponseEntity.status(httpStatus).body(this)
}
