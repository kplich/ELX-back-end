package kplich.backend.configurations.errorhandling

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

@ApiModel(description = "a response containing data about an error")
data class ApiError(
        @JsonIgnore
        val httpStatus: HttpStatus,
        @ApiModelProperty("user-friendly error message")
        val userMessage: String,
        @ApiModelProperty("technical error message")
        val debugMessage: String,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        val timestamp: LocalDateTime = LocalDateTime.now()) {

    @get:JsonIgnore
    val response get(): ResponseEntity<Any> = ResponseEntity.status(httpStatus).body(this)
}
