package com.bookmyevent.eventservice.venue.dto

data class CreateVenueRequest(
    val name: String,
    val city: String,
    val address: String? = null,
    val capacity: Int? = null
)

