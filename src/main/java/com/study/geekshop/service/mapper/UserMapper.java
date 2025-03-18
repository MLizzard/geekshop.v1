package com.study.geekshop.service.mapper;

import com.study.geekshop.model.dto.request.UserRequestDto;
import com.study.geekshop.model.dto.response.UserResponseDto;
import com.study.geekshop.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final OrderMapper orderMapper; // Внедряем OrderMapper

    public User toEntity(UserRequestDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setBirthDate(dto.getBirthDate());
        return user;
    }

    public UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getBirthDate(),
                user.getOrders() != null ? user.getOrders().stream()
                        .map(orderMapper::toDTO) // Здесь вызываем метод маппинга
                        .collect(Collectors.toList())
                        : List.of() // Пустой список, если заказов нет
        );
    }
}
