package com.bookmyevent.seatlockservice.seatlock.redis

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisSeatLockClient(private val redis: StringRedisTemplate) {

    fun tryAcquire(key: String, value: String, ttlSeconds: Long): Boolean {
        val ops = redis.opsForValue()
        return ops.setIfAbsent(key, value, Duration.ofSeconds(ttlSeconds)) ?: false
    }

    fun release(key: String, value: String) {
        val ops = redis.opsForValue()
        val current = ops.get(key)
        if (current != null && current == value) {
            redis.delete(key)
        }
    }
}

