package com.example.e_commmerce.dto;

public record CartItemDTO(
        Long id,
        ProductDTO product,
        Integer quantity
) {}