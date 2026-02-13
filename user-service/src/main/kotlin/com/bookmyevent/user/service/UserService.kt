package com.bookmyevent.user.service

import com.bookmyevent.user.dto.CreateUserRequest
import com.bookmyevent.user.dto.UpdateUserRequest
import com.bookmyevent.user.dto.UserDto

interface UserService {
    fun getUser(id: Long): UserDto
    fun createUser(req: CreateUserRequest): UserDto
    fun updateUser(id: Long, req: UpdateUserRequest): UserDto
}

