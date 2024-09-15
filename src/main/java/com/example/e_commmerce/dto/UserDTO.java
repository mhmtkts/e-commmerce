package com.example.e_commmerce.dto;

import java.util.List;

public record UserDTO(
        String email,
        String password,
        boolean isAdmin,
        RoleDTO role
) {}
