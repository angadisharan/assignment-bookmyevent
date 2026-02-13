package com.bookmyevent.eventcatalog.service.impl

import com.bookmyevent.eventcatalog.dto.EventDto
import com.bookmyevent.eventcatalog.repository.EventRepository
import com.bookmyevent.eventcatalog.service.EventService
import org.springframework.stereotype.Service
import java.util.NoSuchElementException

@Service
class EventServiceImpl(private val eventRepository: EventRepository) : EventService {

    override fun getEvent(id: Long): EventDto {
        val e = eventRepository.findById(id).orElseThrow { NoSuchElementException("Event $id not found") }
        return EventDto(
            id = e.id,
            title = e.title,
            category = e.category,
            startTime = e.startTime,
            endTime = e.endTime
        )
    }
}

