package com.study.geekshop.service;

import com.study.geekshop.model.dto.request.UserRequestDto;
import com.study.geekshop.model.dto.response.UserResponseDto;
import com.study.geekshop.model.entity.User;
import java.util.List;


public interface UserService {
    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long id);

    UserResponseDto createUser(UserRequestDto dto);

    UserResponseDto updateUser(Long id, UserRequestDto dto);

    void deleteUser(Long id);
}

