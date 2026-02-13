package com.bookmyevent.organizer.controller

import com.bookmyevent.eventcatalog.dto.EventDto
import com.bookmyevent.organizer.dto.CreateOrganizerRequest
import com.bookmyevent.organizer.dto.OrganizerDto
import com.bookmyevent.organizer.service.OrganizerService
import com.bookmyevent.eventcatalog.entity.EventEntity
import com.bookmyevent.eventcatalog.repository.EventRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/organizers")
class OrganizerController(
    private val organizerService: OrganizerService,
    private val eventRepository: EventRepository
) {

    @PostMapping
    fun createOrganizer(@RequestBody req: CreateOrganizerRequest): ResponseEntity<OrganizerDto> {
        val dto = organizerService.createOrganizer(req)
        return ResponseEntity.ok(dto)
    }

    @GetMapping("/{id}")
    fun getOrganizer(@PathVariable id: Long): ResponseEntity<OrganizerDto> {
        return ResponseEntity.ok(organizerService.getOrganizer(id))
    }

    @PutMapping("/{id}")
    fun updateOrganizer(@PathVariable id: Long, @RequestBody update: Map<String, String?>): ResponseEntity<OrganizerDto> {
        val orgName = update["orgName"]
        val contactEmail = update["contactEmail"]
        return ResponseEntity.ok(organizerService.updateOrganizer(id, orgName, contactEmail))
    }

    @PostMapping("/{id}/events")
    fun createEvent(@PathVariable id: Long, @RequestBody req: com.bookmyevent.eventcatalog.dto.CreateEventRequest): ResponseEntity<EventDto> {
        val entity = EventEntity(
            organizerId = id,
            venueId = req.venueId,
            title = req.title,
            category = req.category,
            startTime = req.startTime,
            endTime = req.endTime
        )

        val saved = eventRepository.save(entity)
        val resp = EventDto(id = saved.id, title = saved.title, category = saved.category, startTime = saved.startTime, endTime = saved.endTime)
        return ResponseEntity.ok(resp)
    }

    @PutMapping("/{id}/events/{eventId}")
    fun updateEvent(@PathVariable id: Long, @PathVariable eventId: Long, @RequestBody req: EventDto): ResponseEntity<EventDto> {
        val ev = eventRepository.findById(eventId).orElseThrow { NoSuchElementException("Event $eventId not found") }
        val updated = ev.copy(title = req.title, category = req.category, startTime = req.startTime, endTime = req.endTime)
        val saved = eventRepository.save(updated)
        return ResponseEntity.ok(EventDto(id = saved.id, title = saved.title, category = saved.category, startTime = saved.startTime, endTime = saved.endTime))
    }

    @DeleteMapping("/{id}/events/{eventId}")
    fun cancelEvent(@PathVariable id: Long, @PathVariable eventId: Long): ResponseEntity<Void> {
        val ev = eventRepository.findById(eventId).orElseThrow { NoSuchElementException("Event $eventId not found") }
        val cancelled = ev.copy(status = "CANCELLED")
        eventRepository.save(cancelled)
        return ResponseEntity.noContent().build()
    }
}

