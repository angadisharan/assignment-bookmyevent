package com.bookmyevent.eventservice

import com.bookmyevent.eventservice.eventcatalog.dto.EventDto
import com.bookmyevent.eventservice.eventcatalog.dto.SeatDto
import com.bookmyevent.eventservice.eventcatalog.dto.SectionDto
import com.bookmyevent.eventservice.eventcatalog.entity.EventEntity
import com.bookmyevent.eventservice.eventcatalog.entity.EventSectionEntity
import com.bookmyevent.eventservice.eventcatalog.entity.SeatEntity
import com.bookmyevent.eventservice.eventcatalog.repository.EventRepository
import com.bookmyevent.eventservice.eventcatalog.repository.EventSectionRepository
import com.bookmyevent.eventservice.eventcatalog.repository.SeatRepository
import com.bookmyevent.eventservice.eventcatalog.service.impl.EventServiceImpl
import com.bookmyevent.eventservice.venue.repository.VenueRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.LocalDateTime

class EventServiceImplTest {
    private val eventRepo: EventRepository = mock()
    private val venueRepo: VenueRepository = mock()
    private val seatRepo: SeatRepository = mock()
    private val sectionRepo: EventSectionRepository = mock()

    private val service = EventServiceImpl(eventRepo, venueRepo, seatRepo, sectionRepo)

    @Test
    fun `getEvent maps venueCity`() {
        val now = LocalDateTime.now()
        val e = EventEntity(id=1, title="E1", categoryId=1, organizerId=12, venueId=100, status="PUBLISHED", startTime=now, endTime = null)
        whenever(eventRepo.findById(1L)).thenReturn(java.util.Optional.of(e))
        whenever(venueRepo.findById(100L)).thenReturn(java.util.Optional.of(com.bookmyevent.eventservice.venue.entity.VenueEntity(id=100, name="V", city="Mumbai")))

        val dto = service.getEvent(1)
        assertEquals(1, dto.id)
        assertEquals("Mumbai", dto.venueCity)
    }

    @Test
    fun `getSeatMap returns sections and seats`() {
        val sections = listOf(EventSectionEntity(id=1, eventId=1, name="A", capacity=10, notes=null, layoutJson=null))
        val seats = listOf(SeatEntity(id=1, eventId=1, sectionId=1, seatRow="A", seatNumber="1", seatCode="A1", status="AVAILABLE", pricePaisa=1000L, version=0, createdAt=LocalDateTime.now()))
        whenever(sectionRepo.findByEventId(1L)).thenReturn(sections)
        whenever(seatRepo.findByEventId(1L)).thenReturn(seats)

        val (sSecs, sSeats) = service.getSeatMap(1)
        assertEquals(1, sSecs.size)
        assertEquals(1, sSeats.size)
        assertEquals("A1", sSeats[0].seatCode)
    }
}

