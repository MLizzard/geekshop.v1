package com.study.geekshop.model.dto.response;

import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private LocalDate birthDate;
    private List<OrderResponseDto> orders;
}
