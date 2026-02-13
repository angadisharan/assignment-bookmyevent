package com.bookmyevent.eventservice.eventcatalog.dto

data class SectionDto(
    val id: Long,
    val name: String,
    val capacity: Int,
    val notes: String?,
    val layoutJson: String?
)

