package com.bookmyevent.userservice.organizer.controller

import com.bookmyevent.userservice.organizer.dto.CreateOrganizerRequest
import com.bookmyevent.userservice.organizer.dto.OrganizerDto
import com.bookmyevent.userservice.organizer.service.OrganizerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/organizers")
class OrganizerController(private val organizerService: OrganizerService) {

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
}

