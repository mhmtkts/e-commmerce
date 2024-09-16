package com.example.e_commmerce.dto;

public record ProductDTO(
        Long id,
        String name,
        Double price,
        String pictureUrl,
        Long categoryId
) {}