package com.bookmyevent.eventservice.venue.service.impl

import com.bookmyevent.eventservice.venue.dto.CreateVenueRequest
import com.bookmyevent.eventservice.venue.dto.VenueDto
import com.bookmyevent.eventservice.venue.entity.VenueEntity
import com.bookmyevent.eventservice.venue.repository.VenueRepository
import com.bookmyevent.eventservice.venue.service.VenueService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VenueServiceImpl(private val repo: VenueRepository) : VenueService {

    @Transactional
    override fun createVenue(req: CreateVenueRequest): VenueDto {
        val ent = VenueEntity(name = req.name, city = req.city, address = req.address, capacity = req.capacity)
        val saved = repo.save(ent)
        return VenueDto(id = saved.id, name = saved.name, city = saved.city, address = saved.address, capacity = saved.capacity)
    }

    override fun getVenue(id: Long): VenueDto {
        val e = repo.findById(id).orElseThrow { NoSuchElementException("Venue $id not found") }
        return VenueDto(id = e.id, name = e.name, city = e.city, address = e.address, capacity = e.capacity)
    }

    @Transactional
    override fun updateVenue(id: Long, req: CreateVenueRequest): VenueDto {
        val e = repo.findById(id).orElseThrow { NoSuchElementException("Venue $id not found") }
        val updated = e.copy(name = req.name, city = req.city, address = req.address, capacity = req.capacity)
        val saved = repo.save(updated)
        return VenueDto(id = saved.id, name = saved.name, city = saved.city, address = saved.address, capacity = saved.capacity)
    }

    override fun listVenues(): List<VenueDto> {
        return repo.findAll().map { v -> VenueDto(id = v.id, name = v.name, city = v.city, address = v.address, capacity = v.capacity) }
    }
}

