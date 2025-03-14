package com.study.geekshop.controllers;

import com.study.geekshop.model.dto.request.UserRequestDTO;
import com.study.geekshop.model.dto.response.UserResponseDTO;
import com.study.geekshop.service.UserService;
import com.study.geekshop.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userMapper.toDTO(userService.getUserById(id));
    }

    @PostMapping
    public UserResponseDTO createUser(@RequestBody UserRequestDTO dto) {
        return userMapper.toDTO(userService.createUser(dto));
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id, @RequestBody UserRequestDTO dto) {
        return userMapper.toDTO(userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}