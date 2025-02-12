package com.example.Job_Hunter.controller;

import com.example.Job_Hunter.domain.Entity.Permission;
import com.example.Job_Hunter.domain.request.UpdatePermissionReq;
import com.example.Job_Hunter.domain.response.ResultPaginationDTO;
import com.example.Job_Hunter.service.PermissionService;
import com.example.Job_Hunter.utill.exception.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }
    @PostMapping("/permissions")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) throws IdInvalidException {
        return ResponseEntity.ok().body(permissionService.createPermission(permission));
    }

    @GetMapping("permissions")
    public ResponseEntity<ResultPaginationDTO> getAllPermissions(@Filter Specification<Permission> spec, Pageable pageable) {
        return ResponseEntity.ok().body(permissionService.getAllPermissions(spec, pageable));
    }
    @GetMapping("permissions/{id}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(permissionService.getPermissionById(id));
    }
    @PutMapping("permissions")
    public ResponseEntity<Permission> updatePermission(@RequestBody UpdatePermissionReq req) throws IdInvalidException {
        return ResponseEntity.ok().body(permissionService.updatePermission(req));
    }
    @DeleteMapping("permissions/{id}")
    public ResponseEntity<String> deletePermission(@PathVariable("id") Long id) throws IdInvalidException {
        permissionService.deletePermission(id);
        return ResponseEntity.ok().body("Permission deleted");
    }
}
