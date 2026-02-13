package com.bookmyevent.eventservice.eventcatalog.service.impl

import com.bookmyevent.eventservice.eventcatalog.dto.EventDto
import com.bookmyevent.eventservice.eventcatalog.repository.EventRepository
import com.bookmyevent.eventservice.eventcatalog.service.EventService
import org.springframework.stereotype.Service
import java.util.NoSuchElementException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

import com.bookmyevent.eventservice.venue.repository.VenueRepository
import com.bookmyevent.eventservice.eventcatalog.repository.SeatRepository
import com.bookmyevent.eventservice.eventcatalog.repository.EventSectionRepository
import com.bookmyevent.eventservice.eventcatalog.dto.SeatDto
import com.bookmyevent.eventservice.eventcatalog.dto.SectionDto

@Service
class EventServiceImpl(
    private val eventRepository: EventRepository,
    private val venueRepository: VenueRepository,
    private val seatRepo: SeatRepository,
    private val sectionRepo: EventSectionRepository
) : EventService {

    override fun getEvent(id: Long): EventDto {
        val e = eventRepository.findById(id).orElseThrow { NoSuchElementException("Event $id not found") }
        return EventDto(
            id = e.id,
            title = e.title,
            categoryId = e.categoryId,
            venueCity = venueRepository.findById(e.venueId).map { it.city }.orElse(null),
            startTime = e.startTime,
            endTime = e.endTime
        )
    }

    override fun searchEvents(city: String?, categoryId: Long?, dateFrom: LocalDate?, dateTo: LocalDate?): List<EventDto> {
        val startDateTime = dateFrom?.atStartOfDay()
        val endDateTime = dateTo?.atTime(LocalTime.MAX)
        val events = eventRepository.searchEvents(city, categoryId, startDateTime, endDateTime)
        val venueIds = events.map { it.venueId }.distinct()
        val venues = venueRepository.findAllById(venueIds).associateBy { it.id }
        return events.map { e ->
            EventDto(
                id = e.id,
                title = e.title,
                categoryId = e.categoryId,
                venueCity = venues[e.venueId]?.city,
                startTime = e.startTime,
                endTime = e.endTime
            )
        }
    }

    override fun getSeatMap(eventId: Long): Pair<List<SectionDto>, List<SeatDto>> {
        // load sections and seats
        val sections = sectionRepo?.findByEventId(eventId) ?: listOf()
        val seats = seatRepo?.findByEventId(eventId) ?: listOf()
        val secDtos = sections.map { s -> SectionDto(id = s.id, name = s.name, capacity = s.capacity, notes = s.notes, layoutJson = s.layoutJson) }
        val seatDtos = seats.map { s -> SeatDto(id = s.id, seatCode = s.seatCode, row = s.seatRow, number = s.seatNumber, status = s.status, pricePaisa = s.pricePaisa) }
        return Pair(secDtos, seatDtos)
    }
}

