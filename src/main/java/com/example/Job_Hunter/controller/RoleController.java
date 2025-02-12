package com.example.Job_Hunter.controller;

import com.example.Job_Hunter.domain.Entity.Role;
import com.example.Job_Hunter.domain.request.UpdateRoleReq;
import com.example.Job_Hunter.domain.response.ResultPaginationDTO;
import com.example.Job_Hunter.service.RoleService;
import com.example.Job_Hunter.utill.exception.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    @PostMapping("/roles")
    public ResponseEntity<Role> addRole(@RequestBody Role role) throws IdInvalidException {
        return ResponseEntity.ok().body(roleService.createRole(role));
    }
    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable long id) throws IdInvalidException {
        return ResponseEntity.ok().body(roleService.getRoleById(id));
    }
    @GetMapping("/roles")
    public ResponseEntity<ResultPaginationDTO> getAllRoles(@Filter Specification<Role> spec, Pageable pageable) throws IdInvalidException {
        return ResponseEntity.ok().body(roleService.getRole(spec, pageable));
    }
    @PutMapping("/roles")
    public ResponseEntity<Role> updateRole(@RequestBody UpdateRoleReq role) throws IdInvalidException {
        return ResponseEntity.ok().body(roleService.updateRole(role));
    }
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable long id) throws IdInvalidException {
        roleService.deleteRole(id);
        return ResponseEntity.ok().body("Role deleted successfully");
    }

}
