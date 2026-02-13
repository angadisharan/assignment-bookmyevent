package com.bookmyevent.eventcatalog.domain

import java.time.LocalDateTime

data class Event(
    val id: Long,
    val title: String,
    val category: String?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?
)

