package kplich.backend.configurations.errorhandling

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class RestExceptionHandler: ResponseEntityExceptionHandler() {
    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val error = ErrorModel(HttpStatus.BAD_REQUEST, "Validation Error", ex.bindingResult.toString())

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }
}
