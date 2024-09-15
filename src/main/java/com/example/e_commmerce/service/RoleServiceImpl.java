package com.example.e_commmerce.service;

import com.example.e_commmerce.dto.RoleDTO;
import com.example.e_commmerce.entity.Role;
import com.example.e_commmerce.exceptions.ApiException;
import com.example.e_commmerce.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ApiException("Role not found: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public Role createRole(RoleDTO roleDTO) {
        if (roleRepository.findByName(roleDTO.name()).isPresent()) {
            throw new ApiException("Role already exists: " + roleDTO.name(), HttpStatus.CONFLICT);
        }
        Role role = new Role();
        role.setName(roleDTO.name());
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Long id, RoleDTO roleDTO) {
        Role role = getRoleById(id);
        if (!role.getName().equals(roleDTO.name()) &&
                roleRepository.findByName(roleDTO.name()).isPresent()) {
            throw new ApiException("Role name already exists: " + roleDTO.name(), HttpStatus.CONFLICT);
        }
        role.setName(roleDTO.name());
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ApiException("Role not found: " + id, HttpStatus.NOT_FOUND);
        }
        roleRepository.deleteById(id);
    }

    @Override
    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ApiException("User role not found", HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    public Role getAdminRole() {
        return roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new ApiException("Admin role not found", HttpStatus.INTERNAL_SERVER_ERROR));
    }
}