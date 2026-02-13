package com.bookmyevent.bookingservice.booking.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "seat_holds")
data class SeatHoldEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "hold_token", nullable = false)
    val holdToken: String = "",

    @Column(name = "user_id", nullable = false)
    val userId: Long = 0,

    @Column(name = "event_id", nullable = false)
    val eventId: Long = 0,

    @Column(name = "seat_id", nullable = false)
    val seatId: Long = 0,

    @Column(name = "status")
    val status: String = "HOLD",

    @Column(name = "expires_at", nullable = false)
    val expiresAt: LocalDateTime,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)

