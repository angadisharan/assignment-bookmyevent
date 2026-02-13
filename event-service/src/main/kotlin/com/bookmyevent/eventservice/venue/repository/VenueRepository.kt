package com.bookmyevent.eventservice.venue.repository

import com.bookmyevent.eventservice.venue.entity.VenueEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VenueRepository : JpaRepository<VenueEntity, Long>

