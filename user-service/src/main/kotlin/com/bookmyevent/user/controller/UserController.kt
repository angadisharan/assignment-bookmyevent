package com.bookmyevent.user.controller

import com.bookmyevent.user.dto.CreateUserRequest
import com.bookmyevent.user.dto.UpdateUserRequest
import com.bookmyevent.user.dto.UserDto
import com.bookmyevent.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<UserDto> {
        val dto = userService.getUser(id)
        return ResponseEntity.ok(dto)
    }

    @PostMapping
    fun createUser(@RequestBody req: CreateUserRequest): ResponseEntity<UserDto> {
        val dto = userService.createUser(req)
        return ResponseEntity.ok(dto)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody req: UpdateUserRequest): ResponseEntity<UserDto> {
        val dto = userService.updateUser(id, req)
        return ResponseEntity.ok(dto)
    }
}

