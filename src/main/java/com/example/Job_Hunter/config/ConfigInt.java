package com.example.Job_Hunter.config;

import com.example.Job_Hunter.domain.Entity.Permission;
import com.example.Job_Hunter.domain.Entity.Role;
import com.example.Job_Hunter.domain.Entity.User;
import com.example.Job_Hunter.repository.PermissionRepository;
import com.example.Job_Hunter.repository.RoleRepository;
import com.example.Job_Hunter.repository.UserRepository;
import com.example.Job_Hunter.utill.constant.GenderEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Slf4j
public class ConfigInt implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public ConfigInt(RoleRepository roleRepository, PermissionRepository permissionRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Config integration started");
        long countPermissions = permissionRepository.count();
        long countRoles = roleRepository.count();
        long countUsers = userRepository.count();
        if (countPermissions == 0) {
            List<Permission> permissions = new ArrayList<>();
            //Companies
            permissions.add(new Permission("Create a company", "/api/v1/companies", "POST", "COMPANIES"));
            permissions.add(new Permission("Update a company", "/api/v1/companies", "PUT", "COMPANIES"));
            permissions.add(new Permission("Delete a company", "/api/v1/companies/{id}", "DELETE", "COMPANIES"));
            permissions.add(new Permission("Get a company by id", "/api/v1/companies/{id}", "GET", "COMPANIES"));
            permissions.add(new Permission("Get all a company", "/api/v1/companies", "GET", "COMPANIES"));

            //Job
            permissions.add(new Permission("Create a job", "/api/v1/jobs", "POST", "JOBS"));
            permissions.add(new Permission("Update a job", "/api/v1/jobs", "PUT", "JOBS"));
            permissions.add(new Permission("Delete a job", "/api/v1/jobs/{id}", "DELETE", "JOBS"));
            permissions.add(new Permission("Get a job by id", "/api/v1/jobs/{id}", "GET", "JOBS"));
            permissions.add(new Permission("Get all a job", "/api/v1/jobs", "GET", "JOBS"));

            //Permissions
            permissions.add(new Permission("Create a permissions", "/api/v1/permissions", "POST", "PERMISSIONS"));
            permissions.add(new Permission("Update a permissions", "/api/v1/permissions", "PUT", "PERMISSIONS"));
            permissions.add(new Permission("Delete a permissions", "/api/v1/permissions/{id}", "DELETE", "PERMISSIONS"));
            permissions.add(new Permission("Get a permissions by id", "/api/v1/permissions/{id}", "GET", "PERMISSIONS"));
            permissions.add(new Permission("Get all a permissions", "/api/v1/permissions", "GET", "PERMISSIONS"));

            //Resume
            permissions.add(new Permission("Create a resumes", "/api/v1/resumes", "POST", "RESUMES"));
            permissions.add(new Permission("Update a resumes", "/api/v1/resumes", "PUT", "RESUMES"));
            permissions.add(new Permission("Delete a resumes", "/api/v1/resumes/{id}", "DELETE", "RESUMES"));
            permissions.add(new Permission("Get a resumes by id", "/api/v1/resumes/{id}", "GET", "RESUMES"));
            permissions.add(new Permission("Get all a resumes", "/api/v1/resumes", "GET", "RESUMES"));

            //Role
            permissions.add(new Permission("Create a roles", "/api/v1/roles", "POST", "ROLES"));
            permissions.add(new Permission("Update a roles", "/api/v1/roles", "PUT", "ROLES"));
            permissions.add(new Permission("Delete a roles", "/api/v1/roles/{id}", "DELETE", "ROLES"));
            permissions.add(new Permission("Get a roles by id", "/api/v1/roles/{id}", "GET", "ROLES"));
            permissions.add(new Permission("Get all a roles", "/api/v1/roles", "GET", "ROLES"));

            //User
            permissions.add(new Permission("Create a users", "/api/v1/users", "POST", "USERS"));
            permissions.add(new Permission("Update a users", "/api/v1/users", "PUT", "USERS"));
            permissions.add(new Permission("Delete a users", "/api/v1/users/{id}", "DELETE", "USERS"));
            permissions.add(new Permission("Get a users by id", "/api/v1/users/{id}", "GET", "USERS"));
            permissions.add(new Permission("Get all a users", "/api/v1/users", "GET", "USERS"));

            //Subscriber
            permissions.add(new Permission("Create a subscriber", "/api/v1/subscriber", "POST", "SUBSCRIBERS"));
            permissions.add(new Permission("Update a subscriber", "/api/v1/subscriber", "PUT", "SUBSCRIBERS"));
            permissions.add(new Permission("Delete a subscriber", "/api/v1/subscriber/{id}", "DELETE", "SUBSCRIBERS"));
            permissions.add(new Permission("Get a subscriber by id", "/api/v1/subscriber/{id}", "GET", "SUBSCRIBERS"));
            permissions.add(new Permission("Get all a subscriber", "/api/v1/subscriber", "GET", "SUBSCRIBERS"));

            //File
            permissions.add(new Permission("DOWNLOAD FILE", "/api/v1/files", "GET", "FILES"));
            permissions.add(new Permission("UPLOAD FILE", "/api/v1/files", "POST", "FILES"));

            permissionRepository.saveAll(permissions);
        }
        if (countRoles == 0) {
            List<Permission> allPermissions = permissionRepository.findAll();
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            role.setDescription("Admin role");
            role.setActive(true);
            role.setPermissions(allPermissions);
            roleRepository.save(role);
        }
        if (countUsers == 0) {
            User user = new User();
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            user.setUsername("admin@gmail.com");
            user.setEmail("admin@gmail.com");
            user.setAddress("HaTay");
            user.setAge(22);
            user.setGender(GenderEnum.MALE);
            user.setPassword(passwordEncoder.encode("admin"));
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            if (adminRole != null) {
                user.setRole(adminRole);
            }
            userRepository.save(user);

        }
        if (countRoles > 0 && countUsers > 0 && countPermissions > 0) {
            log.info("Skip");
        }
    }
}
