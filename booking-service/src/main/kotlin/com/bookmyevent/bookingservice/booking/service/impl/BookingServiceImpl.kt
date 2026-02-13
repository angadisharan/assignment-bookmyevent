package com.bookmyevent.bookingservice.booking.service.impl

import com.bookmyevent.bookingservice.booking.dto.BookingRequest
import com.bookmyevent.bookingservice.booking.entity.BookingEntity
import com.bookmyevent.bookingservice.booking.repository.BookingRepository
import com.bookmyevent.bookingservice.booking.repository.SeatHoldRepository
import com.bookmyevent.bookingservice.booking.repository.SeatRepository
import com.bookmyevent.bookingservice.booking.repository.BookingItemRepository
import com.bookmyevent.bookingservice.booking.entity.BookingItemEntity
import com.bookmyevent.bookingservice.booking.entity.SeatHoldEntity
import com.bookmyevent.bookingservice.booking.entity.SeatEntity
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
    , private val seatHoldRepository: SeatHoldRepository
    , private val seatRepository: SeatRepository
    , private val bookingItemRepository: BookingItemRepository
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

    @Transactional
    override fun confirmBooking(bookingRef: String, holdToken: String): String {
        // find booking
        val booking = bookingRepository.findAll().find { it.bookingRef == bookingRef }
            ?: throw IllegalArgumentException("Booking not found")

        // find holds
        val holds = seatHoldRepository.findByHoldToken(holdToken)
        if (holds.isEmpty()) throw IllegalArgumentException("Invalid hold token")

        val now = java.time.LocalDateTime.now()
        if (holds.any { it.expiresAt.isBefore(now) }) {
            // release redis locks
            val lr = com.bookmyevent.seatlock.client.LockRequest(booking.eventId, holds.map { it.seatId }, holdToken)
            try { seatLockClient.release(lr) } catch (_: Exception) {}
            throw IllegalStateException("Hold expired")
        }

        // lock and update seats with optimistic locking via JPA @Version
        val seats = holds.map { seatRepository.findById(it.seatId).orElseThrow { IllegalStateException("Seat ${it.seatId} not found") } }
        seats.forEach { s ->
            if (s.status == "BOOKED") {
                throw IllegalStateException("Seat ${s.id} already booked")
            }
            val updated = s.copy(status = "BOOKED", version = s.version + 1)
            seatRepository.save(updated)
        }

        // create booking items
        seats.forEach { s ->
            val price = s.pricePaisa?.toInt() ?: 0
            val item = BookingItemEntity(bookingId = booking.id, seatId = s.id, priceCents = price, status = "RESERVED")
            bookingItemRepository.save(item)
        }

        // mark booking confirmed
        val b = booking.copy(status = "CONFIRMED")
        bookingRepository.save(b)

        // mark holds consumed
        holds.forEach { h ->
            val consumed = h.copy(status = "CONSUMED")
            seatHoldRepository.save(consumed)
        }

        // release redis locks
        val lr = com.bookmyevent.seatlock.client.LockRequest(booking.eventId, holds.map { it.seatId }, holdToken)
        try { seatLockClient.release(lr) } catch (_: Exception) {}

        return bookingRef
    }
}

