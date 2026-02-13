package com.bookmyevent.bookingservice.booking.repository

import com.bookmyevent.bookingservice.booking.entity.SeatHoldEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SeatHoldRepository : JpaRepository<SeatHoldEntity, Long> {
    fun findByHoldToken(holdToken: String): List<SeatHoldEntity>
}

