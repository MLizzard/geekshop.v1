package com.study.geekshop.service;

import com.study.geekshop.model.dto.request.UserRequestDTO;
import com.study.geekshop.model.entity.User;
import java.util.List;


public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User createUser(UserRequestDTO dto);
    User updateUser(Long id, UserRequestDTO dto);
    void deleteUser(Long id);
}

