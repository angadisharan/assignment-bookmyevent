package com.bookmyevent.user.service.impl

import com.bookmyevent.user.dto.CreateUserRequest
import com.bookmyevent.user.dto.UpdateUserRequest
import com.bookmyevent.user.dto.UserDto
import com.bookmyevent.user.entity.UserEntity
import com.bookmyevent.user.repository.UserRepository
import com.bookmyevent.user.service.UserService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException

@Service
class UserServiceImpl(private val userRepository: UserRepository, private val passwordEncoder: BCryptPasswordEncoder) : UserService {

    override fun getUser(id: Long): UserDto {
        val u = userRepository.findById(id).orElseThrow { NoSuchElementException("User $id not found") }
        return UserDto(id = u.id, username = u.username, email = u.email)
    }

    @Transactional
    override fun createUser(req: CreateUserRequest): UserDto {
        val hashed = passwordEncoder.encode(req.password)
        val entity = UserEntity(username = req.username, email = req.email, passwordHash = hashed, fullName = req.fullName)
        try {
            val saved = userRepository.save(entity)
            return UserDto(id = saved.id, username = saved.username, email = saved.email)
        } catch (ex: DataIntegrityViolationException) {
            throw ex
        }
    }

    @Transactional
    override fun updateUser(id: Long, req: UpdateUserRequest): UserDto {
        val u = userRepository.findById(id).orElseThrow { NoSuchElementException("User $id not found") }
        val updated = u.copy(
            email = req.email ?: u.email,
            passwordHash = if (req.password != null) passwordEncoder.encode(req.password) else u.passwordHash,
            // preserve username; fullName optional
            fullName = req.fullName ?: u.fullName
        )
        val saved = userRepository.save(updated)
        return UserDto(id = saved.id, username = saved.username, email = saved.email)
    }
}

