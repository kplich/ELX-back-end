package kplich.backend.configurations.errorhandling

import org.springframework.http.HttpStatus
import java.time.LocalDateTime


data class ErrorModel(
        val httpStatus: HttpStatus,
        val message: String,
        val details: String,
        val timestamp: LocalDateTime = LocalDateTime.now())
