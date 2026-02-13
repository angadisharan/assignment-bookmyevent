package com.bookmyevent.bookingservice.booking.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "booking_items")
data class BookingItemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "booking_id", nullable = false)
    val bookingId: Long = 0,

    @Column(name = "seat_id", nullable = false)
    val seatId: Long = 0,

    @Column(name = "price_cents", nullable = false)
    val priceCents: Int = 0,

    @Column(name = "status")
    val status: String = "RESERVED"
)

