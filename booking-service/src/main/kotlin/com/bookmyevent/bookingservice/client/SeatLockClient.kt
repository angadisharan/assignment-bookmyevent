package com.bookmyevent.seatlock.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

data class LockRequest(val eventId: Long, val seatIds: List<Long>, val holdToken: String, val ttlSeconds: Long = 600L)

@FeignClient(name = "seatlock-service", url = "\${services.seatlock.url}")
interface SeatLockClient {
    @PostMapping("/api/seatlock/tryLock")
    fun tryLock(@RequestBody req: LockRequest): Boolean

    @PostMapping("/api/seatlock/release")
    fun release(@RequestBody req: LockRequest)
}

