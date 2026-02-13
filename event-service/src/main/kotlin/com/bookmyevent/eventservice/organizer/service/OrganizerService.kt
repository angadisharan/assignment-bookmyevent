package com.bookmyevent.eventservice.organizer.service

import com.bookmyevent.eventservice.organizer.dto.CreateOrganizerRequest
import com.bookmyevent.eventservice.organizer.dto.OrganizerDto

interface OrganizerService {
    fun createOrganizer(req: CreateOrganizerRequest): OrganizerDto
    fun getOrganizer(id: Long): OrganizerDto
    fun updateOrganizer(id: Long, orgName: String?, contactEmail: String?): OrganizerDto
}

