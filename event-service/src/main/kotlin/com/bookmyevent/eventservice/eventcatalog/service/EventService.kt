package com.bookmyevent.eventservice.eventcatalog.service

import com.bookmyevent.eventservice.eventcatalog.dto.EventDto

interface EventService {
    fun getEvent(id: Long): EventDto
}

