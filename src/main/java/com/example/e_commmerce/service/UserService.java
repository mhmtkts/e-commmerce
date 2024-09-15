package com.example.e_commmerce.service;

import com.example.e_commmerce.dto.UserDTO;
import com.example.e_commmerce.entity.User;

public interface UserService{
    User getUserById(Long id);
    User createUser(UserDTO userDTO);
    User updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
}
