package com.bookmyevent.seatlock.service

interface SeatLockService {
    /**
     * Try to acquire locks for given seat ids for an event for the given holdToken.
     * Returns true if all locks acquired.
     */
    fun tryLockSeats(eventId: Long, seatIds: List<Long>, holdToken: String, ttlSeconds: Long = 600L): Boolean

    /**
     * Release locks for given seats / holdToken.
     */
    fun releaseLocks(holdToken: String)
}

