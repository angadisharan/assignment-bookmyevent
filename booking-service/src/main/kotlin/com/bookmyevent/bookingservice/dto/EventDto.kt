package com.bookmyevent.bookingservice.dto

import java.time.LocalDateTime

data class EventDto(
    val id: Long,
    val title: String,
    val category: String?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?
)

