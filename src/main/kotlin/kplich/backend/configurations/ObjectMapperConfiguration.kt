package kplich.backend.configurations

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean

class ObjectMapperConfiguration {
    @Bean
    fun getObjectMapper(): ObjectMapper {
        return ObjectMapper().apply {
            registerModule(KotlinModule())
            registerModule(JavaTimeModule())
        }
    }
}
