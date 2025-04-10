package com.study.geekshop.service.impl;

import com.study.geekshop.cache.UserCache;
import com.study.geekshop.exceptions.UserNotFoundException;
import com.study.geekshop.model.dto.request.UserRequestDto;
import com.study.geekshop.model.dto.response.UserResponseDto;
import com.study.geekshop.model.entity.User;
import com.study.geekshop.repository.UserRepository;
import com.study.geekshop.service.UserService;
import com.study.geekshop.service.mapper.UserMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserCache userCache;

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(userMapper::toDto)
            .toList();
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User userFromCache = userCache.get(id);
        if (userFromCache != null) {
            return userMapper.toDto(userFromCache);
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        User user = userMapper.toEntity(dto);
        user = userRepository.save(user);
        userCache.put(user.getId(), user);
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto dto) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        existingUser.setUsername(dto.getUsername());
        existingUser.setEmail(dto.getEmail());
        existingUser.setPassword(dto.getPassword());
        existingUser.setBirthDate(dto.getBirthDate());
        existingUser = userRepository.save(existingUser);
        userCache.put(existingUser.getId(), existingUser);
        return userMapper.toDto(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        userCache.remove(id);
    }
}
