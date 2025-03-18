package com.study.geekshop.service;

import com.study.geekshop.model.dto.request.UserRequestDto;
import com.study.geekshop.model.entity.User;
import java.util.List;


public interface UserService {
    List<User> getAllUsers();

    User getUserById(Long id);

    User createUser(UserRequestDto dto);

    User updateUser(Long id, UserRequestDto dto);

    void deleteUser(Long id);
}

