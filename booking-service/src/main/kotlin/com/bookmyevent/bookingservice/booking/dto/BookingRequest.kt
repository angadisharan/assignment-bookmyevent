package com.bookmyevent.bookingservice.booking.dto

data class BookingRequest(
    val userId: Long,
    val eventId: Long,
    val seatIds: List<Long>,
    val offerCode: String? = null
)

