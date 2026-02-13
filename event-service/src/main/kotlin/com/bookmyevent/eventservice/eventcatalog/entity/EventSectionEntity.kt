package com.bookmyevent.eventservice.eventcatalog.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "event_sections")
data class EventSectionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "event_id", nullable = false)
    val eventId: Long = 0,

    @Column(nullable = false)
    val name: String = "",

    @Column
    val capacity: Int = 0,

    @Column
    val notes: String? = null,

    @Column(name = "layout_json")
    val layoutJson: String? = null
)

