package com.bookmyevent.booking.service.impl

import com.bookmyevent.booking.dto.BookingRequest
import com.bookmyevent.booking.entity.BookingEntity
import com.bookmyevent.booking.repository.BookingRepository
import com.bookmyevent.booking.service.BookingService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class BookingServiceImpl(private val bookingRepository: BookingRepository) : BookingService {

    @Transactional
    override fun createBooking(req: BookingRequest): String {
        // For now create a simple booking record with zero total (pricing not implemented yet)
        val bookingRef = "BKG-" + UUID.randomUUID().toString().substring(0, 8).uppercase()

        val entity = BookingEntity(
            bookingRef = bookingRef,
            userId = req.userId,
            eventId = req.eventId,
            totalAmountCents = 0,
            status = "PENDING"
        )

        bookingRepository.save(entity)
        return bookingRef
    }
}

