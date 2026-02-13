package com.bookmyevent.eventservice.venue.controller

import com.bookmyevent.eventservice.venue.dto.CreateVenueRequest
import com.bookmyevent.eventservice.venue.dto.VenueDto
import com.bookmyevent.eventservice.venue.service.VenueService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/venues")
class VenueController(private val venueService: VenueService) {

    @PostMapping
    fun createVenue(@RequestBody req: CreateVenueRequest): ResponseEntity<VenueDto> {
        val dto = venueService.createVenue(req)
        return ResponseEntity.ok(dto)
    }

    @GetMapping("/{id}")
    fun getVenue(@PathVariable id: Long): ResponseEntity<VenueDto> {
        return ResponseEntity.ok(venueService.getVenue(id))
    }

    @PutMapping("/{id}")
    fun updateVenue(@PathVariable id: Long, @RequestBody req: CreateVenueRequest): ResponseEntity<VenueDto> {
        return ResponseEntity.ok(venueService.updateVenue(id, req))
    }

    @GetMapping
    fun listVenues(): ResponseEntity<List<VenueDto>> {
        return ResponseEntity.ok(venueService.listVenues())
    }
}

