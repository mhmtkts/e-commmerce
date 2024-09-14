package com.example.e_commmerce.dto;

import java.util.List;

public record CategoryDTO(
        Long id,
        String name,
        List<ProductDTO> products
) {}