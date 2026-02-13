package com.bookmyevent.eventservice.eventcatalog.controller

import com.bookmyevent.eventservice.eventcatalog.dto.EventDto
import com.bookmyevent.eventservice.eventcatalog.service.EventService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/events")
class EventController(private val eventService: EventService) {

    @GetMapping("/{id}")
    fun getEvent(@PathVariable id: Long): ResponseEntity<EventDto> {
        val dto = eventService.getEvent(id)
        return ResponseEntity.ok(dto)
    }

}

