package com.bookmyevent.eventservice.eventcatalog.dto

import java.time.LocalDateTime

data class CreateEventRequest(
    val venueId: Long,
    val title: String,
    val category: String?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?
)

