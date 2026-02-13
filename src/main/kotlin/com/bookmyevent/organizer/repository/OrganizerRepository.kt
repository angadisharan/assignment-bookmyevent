package com.bookmyevent.organizer.repository

import com.bookmyevent.organizer.entity.OrganizerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrganizerRepository : JpaRepository<OrganizerEntity, Long>

