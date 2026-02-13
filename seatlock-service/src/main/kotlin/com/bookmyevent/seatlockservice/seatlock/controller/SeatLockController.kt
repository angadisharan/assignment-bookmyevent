package com.bookmyevent.seatlockservice.seatlock.controller

import com.bookmyevent.seatlockservice.seatlock.redis.RedisSeatLockClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class LockRequest(val eventId: Long, val seatIds: List<Long>, val holdToken: String, val ttlSeconds: Long = 600L)

@RestController
@RequestMapping("/api/seatlock")
class SeatLockController(private val redisClient: RedisSeatLockClient) {

    @PostMapping("/tryLock")
    fun tryLock(@RequestBody req: LockRequest): ResponseEntity<Boolean> {
        val acquired = mutableListOf<String>()
        for (seatId in req.seatIds) {
            val key = "lock:event:${req.eventId}:seat:$seatId"
            val ok = redisClient.tryAcquire(key, req.holdToken, req.ttlSeconds)
            if (!ok) {
                // release already acquired
                acquired.forEach { redisClient.release(it, req.holdToken) }
                return ResponseEntity.ok(false)
            } else {
                acquired.add(key)
            }
        }
        return ResponseEntity.ok(true)
    }

    @PostMapping("/release")
    fun release(@RequestBody req: LockRequest): ResponseEntity<Void> {
        for (seatId in req.seatIds) {
            val key = "lock:event:${req.eventId}:seat:$seatId"
            redisClient.release(key, req.holdToken)
        }
        return ResponseEntity.noContent().build()
    }
}

