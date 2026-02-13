package com.bookmyevent.user.service

import com.bookmyevent.user.dto.UserDto

interface UserService {
    fun getUser(id: Long): UserDto
}

