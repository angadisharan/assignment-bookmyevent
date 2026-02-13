package com.bookmyevent.seatlockservice.seatlock.service

interface SeatLockService {
    fun tryLockSeats(eventId: Long, seatIds: List<Long>, holdToken: String, ttlSeconds: Long = 600L): Boolean
    fun releaseLocks(holdToken: String)
}

