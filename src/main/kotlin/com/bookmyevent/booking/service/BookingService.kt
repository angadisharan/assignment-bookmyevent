package com.bookmyevent.booking.service

import com.bookmyevent.booking.dto.BookingRequest

interface BookingService {
    fun createBooking(req: BookingRequest): String
}

