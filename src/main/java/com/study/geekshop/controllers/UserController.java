package com.study.geekshop.controllers;

import com.study.geekshop.model.dto.request.UserRequestDto;
import com.study.geekshop.model.dto.response.UserResponseDto;
import com.study.geekshop.service.UserService;
import com.study.geekshop.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userMapper.toDto(userService.getUserById(id));
    }

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto dto) {
        return userMapper.toDto(userService.createUser(dto));
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody UserRequestDto dto) {
        return userMapper.toDto(userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}