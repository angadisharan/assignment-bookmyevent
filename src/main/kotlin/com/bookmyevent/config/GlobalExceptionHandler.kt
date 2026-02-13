package com.bookmyevent.config

import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.Instant

data class ErrorResponse(val timestamp: Instant = Instant.now(), val status: Int, val error: String, val message: String?)

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException): ResponseEntity<ErrorResponse> {
        val body = ErrorResponse(status = HttpStatus.NOT_FOUND.value(), error = "Not Found", message = ex.message)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrity(ex: DataIntegrityViolationException): ResponseEntity<ErrorResponse> {
        // Try to extract constraint name if available
        val cause = ex.cause
        val constraintInfo = when (cause) {
            is ConstraintViolationException -> cause.constraintName
            else -> null
        }

        val friendlyMessage = when {
            constraintInfo != null -> mapConstraintToMessage(constraintInfo)
            cause?.message != null && cause.message!!.contains("foreign key", ignoreCase = true) ->
                "Foreign key violation (referenced entity not found)"
            else -> null
        }

        val msg = friendlyMessage ?: "Data integrity violation. ${constraintInfo ?: cause?.message ?: ex.message}"
        val body = ErrorResponse(status = HttpStatus.BAD_REQUEST.value(), error = "Bad Request", message = msg)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    private fun mapConstraintToMessage(constraint: String): String {
        val c = constraint.lowercase()
        return when {
            c.contains("user") && c.contains("role") -> "Invalid role or user-role relation"
            c.contains("user") && c.contains("roles").not() && (c.contains("organizers") || c.contains("organizer")) -> "Organizer user not present"
            c.contains("organizers") && c.contains("user") -> "Organizer profile references missing user"
            c.contains("seatholds") && c.contains("user") -> "User holding seat not found"
            c.contains("bookings") && c.contains("user") -> "User placing booking not found"
            c.contains("fk_bookings_user") || c.contains("bookings_user") -> "User referenced in booking not found"
            c.contains("fk_organizers_user") || c.contains("organizers_user") -> "User referenced as organizer not found"
            c.contains("fk_seats_event") || c.contains("seats_event") -> "Event or seat not found"
            c.contains("fk_events_organizer") || c.contains("events_organizer") -> "Organizer not found for event"
            c.contains("fk_seatholds_seat") || c.contains("seatholds_seat") -> "Seat referenced in hold not found"
            c.contains("ux_event_seatcode") || c.contains("uq_event_seat_code") -> "Seat code must be unique for an event"
            else -> "Data integrity violation: $constraint"
        }
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception): ResponseEntity<ErrorResponse> {
        val body = ErrorResponse(status = HttpStatus.INTERNAL_SERVER_ERROR.value(), error = "Internal Server Error", message = ex.message)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body)
    }
}

