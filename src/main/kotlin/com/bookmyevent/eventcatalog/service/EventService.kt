package com.bookmyevent.eventcatalog.service

import com.bookmyevent.eventcatalog.dto.EventDto

interface EventService {
    fun getEvent(id: Long): EventDto
}

