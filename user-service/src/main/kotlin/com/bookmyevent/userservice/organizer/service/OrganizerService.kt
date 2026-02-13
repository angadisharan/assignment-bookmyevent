package com.bookmyevent.userservice.organizer.service

import com.bookmyevent.userservice.organizer.dto.CreateOrganizerRequest
import com.bookmyevent.userservice.organizer.dto.OrganizerDto

interface OrganizerService {
    fun createOrganizer(req: CreateOrganizerRequest): OrganizerDto
    fun getOrganizer(id: Long): OrganizerDto
    fun updateOrganizer(id: Long, orgName: String?, contactEmail: String?): OrganizerDto
}

