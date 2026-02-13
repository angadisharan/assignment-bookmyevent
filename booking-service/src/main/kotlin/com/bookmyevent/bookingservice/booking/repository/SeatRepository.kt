package com.bookmyevent.bookingservice.booking.repository

import com.bookmyevent.bookingservice.booking.entity.SeatEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SeatRepository : JpaRepository<SeatEntity, Long> {
}

