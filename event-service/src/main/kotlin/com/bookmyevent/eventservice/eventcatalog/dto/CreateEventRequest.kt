package com.bookmyevent.eventservice.eventcatalog.dto

import java.time.LocalDateTime

data class CreateEventRequest(
    val organizerId: Long,
    val venueId: Long,
    val title: String,
    val categoryId: Long?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?
)

