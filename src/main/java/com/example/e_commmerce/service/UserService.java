package com.example.e_commmerce.service;

import com.example.e_commmerce.dto.UserDTO;
import com.example.e_commmerce.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService  extends UserDetailsService {
    User getUserById(Long id);
    User createUser(UserDTO userDTO);
    User updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
}
