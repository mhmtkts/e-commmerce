package com.example.e_commmerce.service;

import com.example.e_commmerce.dto.RoleDTO;
import com.example.e_commmerce.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role createRole(RoleDTO roleDTO);
    Role updateRole(Long id, RoleDTO roleDTO);
    void deleteRole(Long id);
    Role getUserRole();
    Role getAdminRole();
}
