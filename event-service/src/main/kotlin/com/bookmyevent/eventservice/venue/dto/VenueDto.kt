package com.bookmyevent.eventservice.venue.dto

data class VenueDto(
    val id: Long,
    val name: String,
    val city: String,
    val address: String?,
    val capacity: Int?
)

