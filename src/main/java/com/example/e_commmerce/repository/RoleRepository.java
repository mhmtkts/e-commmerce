package com.example.e_commmerce.repository;

import com.example.e_commmerce.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);


}
