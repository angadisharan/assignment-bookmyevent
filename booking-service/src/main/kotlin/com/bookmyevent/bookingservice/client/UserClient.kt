package com.bookmyevent.user.client

import com.bookmyevent.bookingservice.dto.UserDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "user-service", url = "\${services.user.url}")
interface UserClient {
    @GetMapping("/api/users/{id}")
    fun getUser(@PathVariable id: Long): UserDto
}

