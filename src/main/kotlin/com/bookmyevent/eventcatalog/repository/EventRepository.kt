package com.bookmyevent.eventcatalog.repository

import com.bookmyevent.eventcatalog.entity.EventEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<EventEntity, Long> {
    // add custom queries as needed
}

