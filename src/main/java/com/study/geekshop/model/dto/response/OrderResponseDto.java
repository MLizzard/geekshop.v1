package com.study.geekshop.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;
    private String username;
    private LocalDateTime orderDate;
    private String status;
    private List<OrderItemResponseDto> items;
}
