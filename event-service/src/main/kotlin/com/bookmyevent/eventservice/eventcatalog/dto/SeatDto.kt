package com.bookmyevent.eventservice.eventcatalog.dto

data class SeatDto(
    val id: Long,
    val seatCode: String,
    val row: String?,
    val number: String?,
    val status: String,
    val pricePaisa: Long?
)

