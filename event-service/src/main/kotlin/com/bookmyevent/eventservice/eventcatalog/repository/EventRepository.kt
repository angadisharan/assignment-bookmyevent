package com.bookmyevent.eventservice.eventcatalog.repository

import com.bookmyevent.eventservice.eventcatalog.entity.EventEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<EventEntity, Long> {
    // add custom queries as needed
}

