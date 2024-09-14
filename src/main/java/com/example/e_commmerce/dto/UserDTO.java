package com.example.e_commmerce.dto;

public record UserDTO(
        Long id,
        String email,
        String password,
        RoleDTO role
) {}
