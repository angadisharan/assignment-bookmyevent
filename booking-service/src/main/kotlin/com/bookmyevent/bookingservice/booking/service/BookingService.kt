package com.bookmyevent.bookingservice.booking.service

import com.bookmyevent.bookingservice.booking.dto.BookingRequest

interface BookingService {
    fun createBooking(req: BookingRequest): String
}

