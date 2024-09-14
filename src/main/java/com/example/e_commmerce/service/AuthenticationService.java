package com.example.e_commmerce.service;

import com.example.e_commmerce.entity.Role;
import com.example.e_commmerce.entity.User;
import com.example.e_commmerce.exceptions.ApiException;
import com.example.e_commmerce.repository.RoleRepository;
import com.example.e_commmerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationService {

    private static final String ROLE_USER = "USER";
    private static final String ROLE_ADMIN = "ADMIN";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            throw new ApiException("User with given email already exists", HttpStatus.CONFLICT);
        }
        String encodedPassword = passwordEncoder.encode(password);

        List<Role> roles = new ArrayList<>();

        addRoleUser(roles);
        addRoleAdmin(roles);

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    private void addRoleAdmin(List<Role> roles) {
        Optional<Role> roleAdmin = roleRepository.findByAuthority(ROLE_ADMIN);
        if (roleAdmin.isEmpty()) {
            Role roleAdminEntity = new Role();
            roleAdminEntity.setName(ROLE_ADMIN);
            roles.add(roleRepository.save(roleAdminEntity));
        } else {
            roles.add(roleAdmin.get());
        }
    }

    private void addRoleUser(List<Role> roles) {
        Optional<Role> userRole = roleRepository.findByAuthority(ROLE_USER);
        if (userRole.isEmpty()) {
            Role roleUserEntity = new Role();
            roleUserEntity.setName(ROLE_USER);
            roles.add(roleRepository.save(roleUserEntity));
        } else {
            roles.add(userRole.get());
        }
    }
}