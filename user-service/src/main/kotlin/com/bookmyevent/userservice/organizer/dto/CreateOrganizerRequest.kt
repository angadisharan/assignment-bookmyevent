package com.bookmyevent.userservice.organizer.dto

data class CreateOrganizerRequest(
    val userId: Long,
    val orgName: String,
    val contactEmail: String?
)

