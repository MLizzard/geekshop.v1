package com.study.geekshop.model.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequestDTO {
    private String username;
    private String email;
    private String password;
    private LocalDate birthDate;
}
