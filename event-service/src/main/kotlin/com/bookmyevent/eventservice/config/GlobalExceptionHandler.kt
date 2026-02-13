package com.bookmyevent.eventservice.config

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.Instant

data class ErrorResponse(val timestamp: Instant = Instant.now(), val status: Int, val error: String, val message: String?)

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val body = ErrorResponse(status = HttpStatus.BAD_REQUEST.value(), error = "Bad Request", message = ex.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrity(ex: DataIntegrityViolationException): ResponseEntity<ErrorResponse> {
        val body = ErrorResponse(status = HttpStatus.BAD_REQUEST.value(), error = "Bad Request", message = "Data integrity violation: ${ex.rootCause?.message ?: ex.message}")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception): ResponseEntity<ErrorResponse> {
        val body = ErrorResponse(status = HttpStatus.INTERNAL_SERVER_ERROR.value(), error = "Internal Server Error", message = ex.message)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body)
    }
}

