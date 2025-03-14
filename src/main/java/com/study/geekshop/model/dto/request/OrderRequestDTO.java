package com.study.geekshop.model.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    private Long userId;
    private List<OrderItemRequestDTO> items;
}
