package com.bookmyevent.bookingservice.booking.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "bookings")
data class BookingEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "booking_ref", nullable = false, unique = true)
    val bookingRef: String = "",

    @Column(name = "user_id", nullable = false)
    val userId: Long = 0,

    @Column(name = "event_id", nullable = false)
    val eventId: Long = 0,

    @Column(name = "total_amount_paisa", nullable = false)
    val totalAmountPaisa: Long = 0L,

    @Column(nullable = false)
    val status: String = "PENDING",

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)

