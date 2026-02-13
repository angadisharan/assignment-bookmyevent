package com.bookmyevent.eventservice.eventcatalog.controller

import com.bookmyevent.eventservice.eventcatalog.dto.CreateEventRequest
import com.bookmyevent.eventservice.eventcatalog.dto.EventDto
import com.bookmyevent.eventservice.eventcatalog.entity.EventEntity
import com.bookmyevent.eventservice.eventcatalog.repository.EventRepository
import com.bookmyevent.eventservice.eventcatalog.service.EventService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/events")
class EventController(
    private val eventService: EventService,
    private val eventRepository: EventRepository,
    private val venueRepository: com.bookmyevent.eventservice.venue.repository.VenueRepository
) {

    @GetMapping("/{id}")
    fun getEvent(@PathVariable id: Long): ResponseEntity<EventDto> {
        val dto = eventService.getEvent(id)
        return ResponseEntity.ok(dto)
    }

    @PostMapping
    fun createEvent(@RequestBody req: CreateEventRequest): ResponseEntity<EventDto> {
        val entity = EventEntity(
            organizerId = req.organizerId,
            venueId = req.venueId,
            title = req.title,
            categoryId = req.categoryId,
            startTime = req.startTime,
            endTime = req.endTime
        )
        val saved = eventRepository.save(entity)
        val venueCity = venueRepository.findById(saved.venueId).map { it.city }.orElse(null)
        val resp = EventDto(id = saved.id, title = saved.title, categoryId = saved.categoryId, venueCity = venueCity, startTime = saved.startTime, endTime = saved.endTime)
        return ResponseEntity.ok(resp)
    }

    @PutMapping("/{id}")
    fun updateEvent(@PathVariable id: Long, @RequestBody req: EventDto): ResponseEntity<EventDto> {
        val ev = eventRepository.findById(id).orElseThrow { NoSuchElementException("Event $id not found") }
        val updated = ev.copy(title = req.title, categoryId = req.categoryId, startTime = req.startTime, endTime = req.endTime)
        val saved = eventRepository.save(updated)
        val venueCity = venueRepository.findById(saved.venueId).map { it.city }.orElse(null)
        return ResponseEntity.ok(EventDto(id = saved.id, title = saved.title, categoryId = saved.categoryId, venueCity = venueCity, startTime = saved.startTime, endTime = saved.endTime))
    }

    @GetMapping
    fun searchEvents(
        @RequestParam(required = false) city: String?,
        @RequestParam(required = false) categoryId: Long?,
        @RequestParam(required = false) dateFrom: String?,
        @RequestParam(required = false) dateTo: String?
    ): ResponseEntity<List<EventDto>> {
        val df = dateFrom?.let { java.time.LocalDate.parse(it) }
        val dt = dateTo?.let { java.time.LocalDate.parse(it) }
        val events = eventService.searchEvents(city, categoryId, df, dt)
        return ResponseEntity.ok(events)
    }
}

