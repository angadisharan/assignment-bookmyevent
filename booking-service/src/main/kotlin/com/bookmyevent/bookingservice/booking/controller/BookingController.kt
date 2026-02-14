package com.bookmyevent.bookingservice.booking.controller

import com.bookmyevent.bookingservice.booking.dto.BookingRequest
import com.bookmyevent.bookingservice.booking.service.BookingService
import com.bookmyevent.bookingservice.dto.BookingConfirmRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/bookings")
class BookingController {

    @Autowired
    private lateinit var bookingService: BookingService

    @PostMapping
    fun createBooking(@RequestBody req: BookingRequest): ResponseEntity<String> {
        val ref = bookingService.createBooking(req)
        return ResponseEntity.ok(ref)
    }

    @PostMapping("/confirm")
    fun confirm(@RequestBody req: BookingConfirmRequest): ResponseEntity<String> {
        val ref = bookingService.confirmBooking(req.bookingRef, req.holdToken)
        return ResponseEntity.ok(ref)
    }
}

