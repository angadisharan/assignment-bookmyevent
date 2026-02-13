package com.bookmyevent.organizer.service.impl

import com.bookmyevent.organizer.dto.CreateOrganizerRequest
import com.bookmyevent.organizer.dto.OrganizerDto
import com.bookmyevent.organizer.entity.OrganizerEntity
import com.bookmyevent.organizer.repository.OrganizerRepository
import com.bookmyevent.organizer.service.OrganizerService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class OrganizerServiceImpl(private val repo: OrganizerRepository) : OrganizerService {

    @Transactional
    override fun createOrganizer(req: CreateOrganizerRequest): OrganizerDto {
        val entity = OrganizerEntity(id = req.id, orgName = req.orgName, contactEmail = req.contactEmail, createdAt = LocalDateTime.now())
        repo.save(entity)
        return OrganizerDto(id = entity.id, orgName = entity.orgName, contactEmail = entity.contactEmail, createdAt = entity.createdAt)
    }

    override fun getOrganizer(id: Long): OrganizerDto {
        val e = repo.findById(id).orElseThrow { NoSuchElementException("Organizer $id not found") }
        return OrganizerDto(id = e.id, orgName = e.orgName, contactEmail = e.contactEmail, createdAt = e.createdAt)
    }

    @Transactional
    override fun updateOrganizer(id: Long, orgName: String?, contactEmail: String?): OrganizerDto {
        val e = repo.findById(id).orElseThrow { NoSuchElementException("Organizer $id not found") }
        val updated = e.copy(orgName = orgName ?: e.orgName, contactEmail = contactEmail ?: e.contactEmail)
        repo.save(updated)
        return OrganizerDto(id = updated.id, orgName = updated.orgName, contactEmail = updated.contactEmail, createdAt = updated.createdAt)
    }
}

