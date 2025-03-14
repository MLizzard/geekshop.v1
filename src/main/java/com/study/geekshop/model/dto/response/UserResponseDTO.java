package com.study.geekshop.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private LocalDate birthDate;
    private List<OrderResponseDTO> orders;
}
