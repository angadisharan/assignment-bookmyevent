package com.bookmyevent.eventservice.eventcatalog.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "seats")
data class SeatEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "event_id", nullable = false)
    val eventId: Long = 0,

    @Column(name = "section_id")
    val sectionId: Long? = null,

    @Column(name = "seat_row")
    val seatRow: String? = null,

    @Column(name = "seat_number")
    val seatNumber: String? = null,

    @Column(name = "seat_code", nullable = false)
    val seatCode: String = "",

    @Column(name = "status")
    val status: String = "AVAILABLE",

    @Column(name = "price_paisa")
    val pricePaisa: Long? = null,

    @Column(name = "version")
    val version: Long = 0,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)

