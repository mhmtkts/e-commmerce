package com.example.e_commmerce.service;

import com.example.e_commmerce.dto.UserDTO;
import com.example.e_commmerce.entity.Role;
import com.example.e_commmerce.entity.User;
import com.example.e_commmerce.exceptions.ApiException;
import com.example.e_commmerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiException("User not found: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public User createUser(UserDTO userDTO) {
        Role role = userDTO.isAdmin() ? roleService.getAdminRole() : roleService.getUserRole();

        User user = new User();
        user.setEmail(userDTO.email());
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        user.setRole(role);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserDTO userDTO) {
        User user = getUserById(id);

        if (!user.getEmail().equals(userDTO.email()) &&
                userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new ApiException("Email already exists: " + userDTO.email(), HttpStatus.CONFLICT);
        }

        user.setEmail(userDTO.email());

        Role role = userDTO.isAdmin() ? roleService.getAdminRole() : roleService.getUserRole();
        user.setRole(role);

        if (userDTO.password() != null && !userDTO.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.password()));
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ApiException("User not found: " + id, HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id);
    }
}