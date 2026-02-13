package com.bookmyevent.bookingservice.booking.service.impl

import com.bookmyevent.bookingservice.booking.dto.BookingRequest
import com.bookmyevent.bookingservice.booking.entity.BookingEntity
import com.bookmyevent.bookingservice.booking.repository.BookingRepository
import com.bookmyevent.bookingservice.booking.service.BookingService
import com.bookmyevent.event.client.EventClient
import com.bookmyevent.seatlock.client.LockRequest
import com.bookmyevent.seatlock.client.SeatLockClient
import com.bookmyevent.user.client.UserClient
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class BookingServiceImpl(
    private val bookingRepository: BookingRepository,
    private val userClient: UserClient,
    private val eventClient: EventClient,
    private val seatLockClient: SeatLockClient
) : BookingService {

    @Transactional
    override fun createBooking(req: BookingRequest): String {
        // validate user exists
        val user = userClient.getUser(req.userId)

        // validate event exists
        val event = eventClient.getEvent(req.eventId)

        // attempt to lock seats via seatlock service
        val holdToken = "HT-" + UUID.randomUUID().toString().substring(0, 8).uppercase()
        val lockReq = LockRequest(req.eventId, req.seatIds, holdToken)
        val ok = seatLockClient.tryLock(lockReq)
        if (!ok) {
            throw IllegalStateException("Unable to acquire seat locks")
        }

        // create booking record (price calculation omitted)
        val bookingRef = "BKG-" + UUID.randomUUID().toString().substring(0, 8).uppercase()
        val entity = BookingEntity(
            bookingRef = bookingRef,
            userId = req.userId,
            eventId = req.eventId,
            totalAmountPaisa = 0L,
            status = "PENDING"
        )
        bookingRepository.save(entity)
        return bookingRef
    }
}

