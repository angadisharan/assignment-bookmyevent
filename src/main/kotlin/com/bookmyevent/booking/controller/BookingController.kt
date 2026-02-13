package com.bookmyevent.booking.controller

import com.bookmyevent.booking.dto.BookingRequest
import com.bookmyevent.booking.service.BookingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/bookings")
class BookingController(private val bookingService: BookingService) {

    @PostMapping
    fun createBooking(@RequestBody req: BookingRequest): ResponseEntity<String> {
        val ref = bookingService.createBooking(req)
        return ResponseEntity.ok(ref)
    }
}

