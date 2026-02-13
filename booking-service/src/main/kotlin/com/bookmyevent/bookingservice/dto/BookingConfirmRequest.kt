package com.bookmyevent.bookingservice.dto

data class BookingConfirmRequest(
    val bookingRef: String,
    val holdToken: String
)

