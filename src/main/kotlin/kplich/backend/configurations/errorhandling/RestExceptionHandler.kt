package kplich.backend.configurations.errorhandling

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(
            ex: MethodArgumentNotValidException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        val error = ApiError(
                status,
                VALIDATION_ERROR_MESSAGE,
                ex.bindingResult
                        .allErrors
                        .map { error -> error.defaultMessage }
                        .joinToString("\n"),
                ex.bindingResult.toString()
        )

        return error.response
    }

    override fun handleHttpMessageNotReadable(
            ex: HttpMessageNotReadableException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        val error = ApiError(
                status,
                MALFORMED_JSON_MESSAGE,
                ex.cause?.message ?: ex.message ?: "",
                ex.cause?.message ?: ex.message ?: "")

        return error.response
    }

    companion object {
        const val VALIDATION_ERROR_MESSAGE = "Validation Error"
        const val MALFORMED_JSON_MESSAGE = "Malformed JSON"
    }
}
