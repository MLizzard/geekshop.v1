package com.study.geekshop.model.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;
    private String username;
    private LocalDateTime orderDate;
    private String status;
    private List<OrderItemResponseDto> items;
}
