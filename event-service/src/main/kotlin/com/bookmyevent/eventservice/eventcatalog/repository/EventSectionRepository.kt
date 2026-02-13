package com.bookmyevent.eventservice.eventcatalog.repository

import com.bookmyevent.eventservice.eventcatalog.entity.EventSectionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventSectionRepository : JpaRepository<EventSectionEntity, Long> {
    fun findByEventId(eventId: Long): List<EventSectionEntity>
}

