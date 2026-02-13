package com.bookmyevent.eventservice.venue.service

import com.bookmyevent.eventservice.venue.dto.CreateVenueRequest
import com.bookmyevent.eventservice.venue.dto.VenueDto

interface VenueService {
    fun createVenue(req: CreateVenueRequest): VenueDto
    fun getVenue(id: Long): VenueDto
    fun updateVenue(id: Long, req: CreateVenueRequest): VenueDto
    fun listVenues(): List<VenueDto>
}

