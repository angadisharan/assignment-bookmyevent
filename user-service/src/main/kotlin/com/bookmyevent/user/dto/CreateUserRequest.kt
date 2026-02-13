package com.bookmyevent.user.dto

data class CreateUserRequest(
    val username: String,
    val email: String,
    val password: String,
    val fullName: String? = null
)

