package com.bookmyevent.eventservice.eventcatalog.repository
import com.bookmyevent.eventservice.eventcatalog.entity.EventEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface EventRepository : JpaRepository<EventEntity, Long> {

    @Query(
        value = """
        SELECT e.* FROM events e
        JOIN venues v ON e.venue_id = v.id
        WHERE (:city IS NULL OR v.city = :city)
          AND (:categoryId IS NULL OR e.category_id = :categoryId)
          AND (:start IS NULL OR e.start_time >= :start)
          AND (:end IS NULL OR e.start_time <= :end)
        """,
        nativeQuery = true
    )
    fun searchEvents(
        @Param("city") city: String?,
        @Param("categoryId") categoryId: Long?,
        @Param("start") start: LocalDateTime?,
        @Param("end") end: LocalDateTime?
    ): List<EventEntity>
}

