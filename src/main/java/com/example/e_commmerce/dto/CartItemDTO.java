package com.example.e_commmerce.dto;

public record CartItemDTO(
        Long userId,
        Long productId,
        Integer quantity
) {}