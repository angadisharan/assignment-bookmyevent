package com.bookmyevent.organizer.dto

import java.time.LocalDateTime

data class OrganizerDto(
    val id: Long,
    val orgName: String,
    val contactEmail: String?,
    val createdAt: LocalDateTime?
)

