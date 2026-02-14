package com.bookmyevent.eventservice.eventcatalog.controller

import com.bookmyevent.eventservice.eventcatalog.dto.CreateEventRequest
import com.bookmyevent.eventservice.eventcatalog.dto.EventDto
import com.bookmyevent.eventservice.eventcatalog.service.EventService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/events")
class EventController {

    @Autowired
    private lateinit var eventService: EventService

    @GetMapping("/{id}")
    fun getEvent(@PathVariable id: Long): ResponseEntity<EventDto> {
        val dto = eventService.getEvent(id)
        return ResponseEntity.ok(dto)
    }

    @PostMapping
    fun createEvent(@RequestBody req: CreateEventRequest): ResponseEntity<EventDto> {
        val resp = eventService.createEvent(req)
        return ResponseEntity.ok(resp)
    }

    @PutMapping("/{id}")
    fun updateEvent(@PathVariable id: Long, @RequestBody req: EventDto): ResponseEntity<EventDto> {
        val resp = eventService.updateEvent(id, req)
        return ResponseEntity.ok(resp)
    }

    @GetMapping("/search")
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

