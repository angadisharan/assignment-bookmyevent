package com.bookmyevent.bookingservice.booking.repository

import com.bookmyevent.bookingservice.booking.entity.BookingItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookingItemRepository : JpaRepository<BookingItemEntity, Long>

