package com.bookmyevent.user.service.impl

import com.bookmyevent.user.dto.UserDto
import com.bookmyevent.user.repository.UserRepository
import com.bookmyevent.user.service.UserService
import org.springframework.stereotype.Service
import java.util.NoSuchElementException

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    override fun getUser(id: Long): UserDto {
        val u = userRepository.findById(id).orElseThrow { NoSuchElementException("User $id not found") }
        return UserDto(id = u.id, username = u.username, email = u.email)
    }
}

