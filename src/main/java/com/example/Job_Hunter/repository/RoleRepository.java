package com.example.Job_Hunter.repository;

import com.example.Job_Hunter.domain.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    boolean existsByName(String name);

    Role findByName(String name);
}
