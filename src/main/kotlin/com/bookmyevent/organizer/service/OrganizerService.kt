package com.bookmyevent.organizer.service

import com.bookmyevent.organizer.dto.CreateOrganizerRequest
import com.bookmyevent.organizer.dto.OrganizerDto

interface OrganizerService {
    fun createOrganizer(req: CreateOrganizerRequest): OrganizerDto
    fun getOrganizer(id: Long): OrganizerDto
    fun updateOrganizer(id: Long, orgName: String?, contactEmail: String?): OrganizerDto
}

