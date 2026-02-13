package com.bookmyevent.bookingservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients(basePackages = ["com.bookmyevent.bookingservice.client", "com.bookmyevent.user.client", "com.bookmyevent.event.client", "com.bookmyevent.seatlock.client"])
@SpringBootApplication
class BookingServiceApplication

fun main(args: Array<String>) {
    runApplication<BookingServiceApplication>(*args)
}

