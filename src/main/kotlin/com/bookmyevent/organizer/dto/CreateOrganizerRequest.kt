package com.bookmyevent.organizer.dto

data class CreateOrganizerRequest(
    val id: Long,
    val orgName: String,
    val contactEmail: String?
)

