package com.example.e_commmerce.dto;

public record UserDTO(
        String email,
        String password,
        RoleDTO role
) {}
