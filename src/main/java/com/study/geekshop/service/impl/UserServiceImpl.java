package com.study.geekshop.service.impl;

import com.study.geekshop.exceptions.UserNotFoundException;
import com.study.geekshop.model.dto.request.UserRequestDTO;
import com.study.geekshop.model.entity.User;
import com.study.geekshop.repository.UserRepository;
import com.study.geekshop.service.UserService;
import com.study.geekshop.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public User createUser(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserRequestDTO dto) {
        User existingUser = getUserById(id);
        existingUser.setUsername(dto.getUsername());
        existingUser.setEmail(dto.getEmail());
        existingUser.setPassword(dto.getPassword());
        existingUser.setBirthDate(dto.getBirthDate());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
