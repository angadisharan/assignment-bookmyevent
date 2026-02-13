package com.bookmyevent.userservice.organizer.service.impl

import com.bookmyevent.userservice.organizer.dto.CreateOrganizerRequest
import com.bookmyevent.userservice.organizer.dto.OrganizerDto
import com.bookmyevent.userservice.organizer.entity.OrganizerEntity
import com.bookmyevent.userservice.organizer.repository.OrganizerRepository
import com.bookmyevent.userservice.organizer.service.OrganizerService
import com.bookmyevent.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class OrganizerServiceImpl(private val repo: OrganizerRepository, private val userRepo: UserRepository) : OrganizerService {

    @Transactional
    override fun createOrganizer(req: CreateOrganizerRequest): OrganizerDto {
        val exists = userRepo.existsById(req.userId)
        if (!exists) throw IllegalArgumentException("User with id ${req.userId} not present")
        val entity = OrganizerEntity(id = req.userId, orgName = req.orgName, contactEmail = req.contactEmail, createdAt = LocalDateTime.now())
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

