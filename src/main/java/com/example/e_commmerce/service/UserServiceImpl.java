package com.example.e_commmerce.service;

import com.example.e_commmerce.dto.RoleDTO;
import com.example.e_commmerce.dto.UserDTO;
import com.example.e_commmerce.entity.Role;
import com.example.e_commmerce.entity.User;
import com.example.e_commmerce.exceptions.ApiException;
import com.example.e_commmerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiException("User not found: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public User createUser(UserDTO userDTO) {
        List<Role> roles = new ArrayList<>();

        for (RoleDTO roleDTO : userDTO.roles()) {
            Role role = roleService.getRoleById(roleDTO.id());
            if (role == null) {
                throw new ApiException("Role not found: " + roleDTO.id(), HttpStatus.BAD_REQUEST);
            }
            roles.add(role);
        }

        User user = new User();
        user.setEmail(userDTO.email());
        user.setPassword(userDTO.password()); // Şifreleme olmadan
        user.setRoles(roles); // Rolleri ayarlama

        return userRepository.save(user); // Kullanıcıyı veritabanına kaydet
    }


    @Override
    public User updateUser(Long id, UserDTO userDTO) {
        User user = getUserById(id);

        if (!user.getEmail().equals(userDTO.email()) &&
                userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new ApiException("Email already exists: " + userDTO.email(), HttpStatus.CONFLICT);
        }

        List<Role> roles = new ArrayList<>();
        for (RoleDTO roleDTO : userDTO.roles()) {
            Role role = roleService.getRoleById(roleDTO.id());
            if (role == null) {
                throw new ApiException("Role not found: " + roleDTO.id(), HttpStatus.BAD_REQUEST);
            }
            roles.add(role);
        }

        user.setEmail(userDTO.email());
        user.setRoles(roles); // Rolleri güncelleyin

        if (userDTO.password() != null && !userDTO.password().isEmpty()) {
            user.setPassword(userDTO.password()); // Şifreleme olmadan
        }

        return userRepository.save(user); // Kullanıcıyı veritabanına kaydedin
    }




    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ApiException("User not found: " + id, HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id);
    }
}
