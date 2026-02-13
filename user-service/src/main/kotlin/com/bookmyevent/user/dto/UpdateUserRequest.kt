package com.bookmyevent.user.dto

data class UpdateUserRequest(
    val email: String?,
    val password: String?,
    val fullName: String?
)

