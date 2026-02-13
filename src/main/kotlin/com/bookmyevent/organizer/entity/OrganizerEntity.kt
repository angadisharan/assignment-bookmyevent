package com.bookmyevent.organizer.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "organizers")
data class OrganizerEntity(
    @Id
    val id: Long = 0, // maps to users.id

    @Column(name = "org_name", nullable = false)
    val orgName: String = "",

    @Column(name = "contact_email")
    val contactEmail: String? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)

