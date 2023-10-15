package dev.mateux.espresso.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<*> {
        val errorModel = exception.message?.let { ErrorDetails(500, it) } ?: ErrorDetails(500, "Internal Server Error")
        return ResponseEntity<Any>(errorModel, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
