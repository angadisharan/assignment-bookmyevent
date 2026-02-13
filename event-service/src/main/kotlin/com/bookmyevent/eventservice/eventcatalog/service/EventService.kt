package com.bookmyevent.eventservice.eventcatalog.service

import com.bookmyevent.eventservice.eventcatalog.dto.EventDto
import com.bookmyevent.eventservice.eventcatalog.dto.SectionDto
import com.bookmyevent.eventservice.eventcatalog.dto.SeatDto
import java.time.LocalDate

interface EventService {
    fun getEvent(id: Long): EventDto
    fun searchEvents(city: String?, categoryId: Long?, dateFrom: LocalDate?, dateTo: LocalDate?): List<EventDto>
    fun getSeatMap(eventId: Long): Pair<List<SectionDto>, List<SeatDto>>
}

