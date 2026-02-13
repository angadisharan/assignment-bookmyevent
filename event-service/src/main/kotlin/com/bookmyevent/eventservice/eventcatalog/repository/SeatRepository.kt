package com.bookmyevent.eventservice.eventcatalog.repository

import com.bookmyevent.eventservice.eventcatalog.entity.SeatEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SeatRepository : JpaRepository<SeatEntity, Long> {
    fun findByEventId(eventId: Long): List<SeatEntity>
    fun findByEventIdAndStatus(eventId: Long, status: String): List<SeatEntity>
}

