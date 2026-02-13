package com.bookmyevent.eventservice.eventcatalog.controller

import com.bookmyevent.eventservice.eventcatalog.dto.SeatDto
import com.bookmyevent.eventservice.eventcatalog.dto.SectionDto
import com.bookmyevent.eventservice.eventcatalog.service.EventService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/events")
class SeatMapController(private val eventService: EventService) {

    @GetMapping("/{id}/seats")
    fun getSeatMap(@PathVariable id: Long): ResponseEntity<Map<String, Any>> {
        val (sections, seats) = eventService.getSeatMap(id)
        val resp = mapOf("sections" to sections, "seats" to seats)
        return ResponseEntity.ok(resp)
    }
}

