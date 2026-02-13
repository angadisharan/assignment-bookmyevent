package com.bookmyevent.seatlockservice

import com.bookmyevent.seatlockservice.seatlock.redis.RedisSeatLockClient

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import java.time.Duration

class RedisSeatLockClientTest {
    private val redisTemplate: StringRedisTemplate = mock()
    private val ops: ValueOperations<String, String> = mock()
    private val client = RedisSeatLockClient(redisTemplate)

    @Test
    fun `tryAcquire returns true when setIfAbsent true`() {
        whenever(redisTemplate.opsForValue()).thenReturn(ops)
        whenever(ops.setIfAbsent("k","v", Duration.ofSeconds(600))).thenReturn(true)
        val ok = client.tryAcquire("k","v",600)
        assertTrue(ok)
    }

    @Test
    fun `tryAcquire returns false when setIfAbsent null or false`() {
        whenever(redisTemplate.opsForValue()).thenReturn(ops)
        whenever(ops.setIfAbsent("k","v", Duration.ofSeconds(600))).thenReturn(null)
        val ok = client.tryAcquire("k","v",600)
        assertFalse(ok)
    }
}

