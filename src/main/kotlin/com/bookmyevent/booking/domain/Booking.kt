package com.bookmyevent.booking.domain

import java.time.LocalDateTime

data class Booking(
    val id: Long,
    val bookingRef: String,
    val userId: Long,
    val eventId: Long,
    val totalCents: Int,
    val status: String,
    val createdAt: LocalDateTime
)

