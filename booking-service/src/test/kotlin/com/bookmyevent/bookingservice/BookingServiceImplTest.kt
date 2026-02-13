package com.bookmyevent.bookingservice

import com.bookmyevent.bookingservice.booking.dto.BookingRequest
import com.bookmyevent.bookingservice.booking.entity.BookingEntity
import com.bookmyevent.bookingservice.booking.entity.SeatEntity
import com.bookmyevent.bookingservice.booking.entity.SeatHoldEntity
import com.bookmyevent.bookingservice.booking.repository.BookingRepository
import com.bookmyevent.bookingservice.booking.repository.BookingItemRepository
import com.bookmyevent.bookingservice.booking.repository.SeatHoldRepository
import com.bookmyevent.bookingservice.booking.repository.SeatRepository
import com.bookmyevent.bookingservice.booking.service.impl.BookingServiceImpl
import com.bookmyevent.event.client.EventClient
import com.bookmyevent.bookingservice.dto.EventDto
import com.bookmyevent.seatlock.client.SeatLockClient
import com.bookmyevent.user.client.UserClient
import com.bookmyevent.bookingservice.dto.UserDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import java.time.LocalDateTime

class BookingServiceImplTest {

    private val bookingRepository: BookingRepository = mock()
    private val userClient: UserClient = mock()
    private val eventClient: EventClient = mock()
    private val seatLockClient: SeatLockClient = mock()
    private val seatHoldRepository: SeatHoldRepository = mock()
    private val seatRepository: SeatRepository = mock()
    private val bookingItemRepository: BookingItemRepository = mock()

    private val service = BookingServiceImpl(
        bookingRepository,
        userClient,
        eventClient,
        seatLockClient,
        seatHoldRepository,
        seatRepository,
        bookingItemRepository
    )

    @Test
    fun `createBooking success`() {
        val req = BookingRequest(userId = 10L, eventId = 100L, seatIds = listOf(1L,2L))
        whenever(userClient.getUser(10L)).thenReturn(UserDto(10L,"alice","alice@example.com"))
        whenever(eventClient.getEvent(100L)).thenReturn(EventDto(100L,"E","", LocalDateTime.now(), null))
        whenever(seatLockClient.tryLock(any())).thenReturn(true)
        whenever(bookingRepository.save(any())).thenAnswer { it.arguments[0] as BookingEntity }

        val ref = service.createBooking(req)
        assertTrue(ref.startsWith("BKG-"))
        verify(bookingRepository, times(1)).save(any())
    }

    @Test
    fun `createBooking lock fail`() {
        val req = BookingRequest(userId = 10L, eventId = 100L, seatIds = listOf(1L,2L))
        whenever(userClient.getUser(10L)).thenReturn(UserDto(10L,"alice","alice@example.com"))
        whenever(eventClient.getEvent(100L)).thenReturn(EventDto(100L,"E","", LocalDateTime.now(), null))
        whenever(seatLockClient.tryLock(any())).thenReturn(false)

        assertThrows<IllegalStateException> {
            service.createBooking(req)
        }
        verify(bookingRepository, never()).save(any())
    }

    @Test
    fun `confirmBooking success`() {
        val booking = BookingEntity(id=1, bookingRef="REF1", userId=10, eventId=100, totalAmountPaisa=0, status="PENDING")
        whenever(bookingRepository.findAll()).thenReturn(listOf(booking))

        val hold = SeatHoldEntity(id=1, holdToken="HT1", userId=10, eventId=100, seatId=1, status="HOLD", expiresAt = LocalDateTime.now().plusMinutes(5))
        whenever(seatHoldRepository.findByHoldToken("HT1")).thenReturn(listOf(hold))

        val seat = SeatEntity(id=1, eventId=100, seatCode="A1", status="AVAILABLE", createdAt=LocalDateTime.now())
        whenever(seatRepository.findById(1L)).thenReturn(java.util.Optional.of(seat))

        // mocks for save operations
        whenever(seatRepository.save(any())).thenAnswer { it.arguments[0] as SeatEntity }
        whenever(bookingItemRepository.save(any())).thenAnswer { it.arguments[0] as Any }
        whenever(bookingRepository.save(any())).thenAnswer { it.arguments[0] as BookingEntity }
        whenever(seatHoldRepository.save(any())).thenAnswer { it.arguments[0] as SeatHoldEntity }

        val res = service.confirmBooking("REF1", "HT1")
        assertEquals("REF1", res)
        verify(seatRepository, atLeastOnce()).save(any())
        verify(bookingItemRepository, atLeastOnce()).save(any())
    }

    @Test
    fun `confirmBooking hold expired`() {
        val booking = BookingEntity(id=1, bookingRef="REF1", userId=10, eventId=100, totalAmountPaisa=0, status="PENDING")
        whenever(bookingRepository.findAll()).thenReturn(listOf(booking))
        val hold = SeatHoldEntity(id=1, holdToken="HT1", userId=10, eventId=100, seatId=1, status="HOLD", expiresAt = LocalDateTime.now().minusMinutes(1))
        whenever(seatHoldRepository.findByHoldToken("HT1")).thenReturn(listOf(hold))

        assertThrows<IllegalStateException> {
            service.confirmBooking("REF1", "HT1")
        }
        verify(seatLockClient, atLeastOnce()).release(any())
    }
}

