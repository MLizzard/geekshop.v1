package com.study.geekshop.service.impl;

import com.study.geekshop.cache.UserCache;
import com.study.geekshop.exceptions.UserNotFoundException;
import com.study.geekshop.model.dto.request.UserRequestDto;
import com.study.geekshop.model.dto.response.UserResponseDto;
import com.study.geekshop.model.entity.User;
import com.study.geekshop.repository.UserRepository;
import com.study.geekshop.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private UserCache userCache;

    @InjectMocks private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("pass");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        return user;
    }
    @Test
    void getAllUsers_shouldReturnMappedList() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("john");
        user1.setEmail("john@example.com");
        user1.setPassword("pass");
        user1.setBirthDate(LocalDate.of(2000, 1, 1));
        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("jane");
        user2.setEmail("jane@example.com");
        user2.setPassword("pass");
        user2.setBirthDate(LocalDate.of(1990, 5, 5));
        List<User> users = List.of(
               user1, user2
        );
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(any()))
                .thenReturn(new UserResponseDto(1L, "john", "john@example.com", LocalDate.of(2000, 1, 1), null))
                .thenReturn(new UserResponseDto(2L, "jane", "jane@example.com", LocalDate.of(1990, 5, 5), null));

        List<UserResponseDto> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userMapper, times(2)).toDto(any());
    }

    @Test
    void getUserById_shouldReturnFromCache() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("pass");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        UserResponseDto dto = new UserResponseDto(1L, "john", "john@example.com", LocalDate.of(2000, 1, 1), null);

        when(userCache.get(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        UserResponseDto result = userService.getUserById(1L);

        assertEquals(dto, result);
        verify(userRepository, never()).findById(any());
    }

    @Test
    void getUserById_shouldFetchFromDbIfCacheMiss() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("pass");
        user.setBirthDate(LocalDate.of(2000, 1, 1));

        UserResponseDto dto = new UserResponseDto(1L, "john", "john@example.com", LocalDate.of(2000, 1, 1), null);

        when(userCache.get(1L)).thenReturn(null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        UserResponseDto result = userService.getUserById(1L);

        assertEquals(dto, result);
    }

    @Test
    void getUserById_shouldThrowIfNotFound() {
        when(userCache.get(1L)).thenReturn(null);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void createUser_shouldMapSaveAndCache() {
        UserRequestDto request = new UserRequestDto();
        request.setUsername("john");
        request.setEmail("john@example.com");
        request.setPassword("pass");
        request.setBirthDate(LocalDate.of(2000, 1, 1));
        User entity = createTestUser();
        UserResponseDto dto = new UserResponseDto(1L, "john", "john@example.com", LocalDate.of(2000, 1, 1), null);

        when(userMapper.toEntity(request)).thenReturn(entity);
        when(userRepository.save(entity)).thenReturn(entity);
        when(userMapper.toDto(entity)).thenReturn(dto);

        UserResponseDto result = userService.createUser(request);

        assertEquals(dto, result);
        verify(userCache).put(1L, entity);
    }

    @Test
    void updateUser_shouldUpdateEntity() {
        UserRequestDto request = new UserRequestDto();
        request.setUsername("updated");
        request.setEmail("upd@example.com");
        request.setPassword("newpass");
        request.setBirthDate(LocalDate.of(1995, 3, 10));
        User existing = createTestUser();
        UserResponseDto dto = new UserResponseDto(1L, "updated", "upd@example.com", LocalDate.of(1995, 3, 10), null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);
        when(userMapper.toDto(existing)).thenReturn(dto);

        UserResponseDto result = userService.updateUser(1L, request);

        assertEquals(dto, result);
        assertEquals("updated", existing.getUsername());
        assertEquals("upd@example.com", existing.getEmail());
        assertEquals("newpass", existing.getPassword());
        assertEquals(LocalDate.of(1995, 3, 10), existing.getBirthDate());
        verify(userCache).put(1L, existing);
    }

    @Test
    void updateUser_shouldThrowIfNotFound() {
        UserRequestDto request = new UserRequestDto();
        request.setUsername("updated");
        request.setEmail("upd@example.com");
        request.setPassword("newpass");
        request.setBirthDate(LocalDate.now());
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, request));
    }

    @Test
    void deleteUser_shouldDeleteAndEvictFromCache() {
        userService.deleteUser(42L);

        verify(userRepository).deleteById(42L);
        verify(userCache).remove(42L);
    }
}
