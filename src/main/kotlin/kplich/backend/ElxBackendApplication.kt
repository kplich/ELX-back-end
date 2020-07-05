package kplich.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ElxBackendApplication

fun main(args: Array<String>) {
	runApplication<ElxBackendApplication>(*args)
}
