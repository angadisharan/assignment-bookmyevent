package com.bookmyevent.event.client

import com.bookmyevent.bookingservice.dto.EventDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "event-service", url = "\${services.event.url}")
interface EventClient {
    @GetMapping("/api/events/{id}")
    fun getEvent(@PathVariable id: Long): EventDto
}

