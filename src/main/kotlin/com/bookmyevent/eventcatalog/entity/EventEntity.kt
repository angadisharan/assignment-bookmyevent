package com.bookmyevent.eventcatalog.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "events")
data class EventEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val title: String = "",

    @Column
    val category: String? = null,

    @Column(name = "organizer_id", nullable = false)
    val organizerId: Long = 0,

    @Column(name = "venue_id", nullable = false)
    val venueId: Long = 0,

    @Column(nullable = false)
    val status: String = "DRAFT",

    @Column(name = "start_time", nullable = false)
    val startTime: LocalDateTime = LocalDateTime.now(),

    @Column(name = "end_time")
    val endTime: LocalDateTime? = null
)

