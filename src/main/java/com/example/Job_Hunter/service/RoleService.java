package com.example.Job_Hunter.service;

import com.example.Job_Hunter.domain.Entity.Permission;
import com.example.Job_Hunter.domain.Entity.Role;
import com.example.Job_Hunter.domain.request.UpdateRoleReq;
import com.example.Job_Hunter.domain.response.ResultPaginationDTO;
import com.example.Job_Hunter.repository.PermissionRepository;
import com.example.Job_Hunter.repository.RoleRepository;
import com.example.Job_Hunter.utill.exception.IdInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Role createRole(Role role) throws IdInvalidException {
        if (roleRepository.existsByName(role.getName())) {
            throw new IdInvalidException("Role already exists");
        }
        if (role.getPermissions() != null) {
            List<Long> reqPermissions = role.getPermissions().stream()
                    .map(Permission::getId)
                    .toList();
            List<Permission> dbPermissions = permissionRepository.findByIdIn(reqPermissions);
            role.setPermissions(dbPermissions);
        }
        return roleRepository.save(role);
    }
    public Role getRoleById(Long id) throws IdInvalidException {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            return role.get();
        }
        throw new IdInvalidException("Role not found");
    }

    public Role updateRole(UpdateRoleReq req) throws IdInvalidException {
        Role role = getRoleById(req.getId());
        if (role.getPermissions() != null) {
            List<Long> permission = req.getPermissions().stream()
                    .map(Permission::getId)
                    .toList();
            List<Permission> dbPermissions = permissionRepository.findByIdIn(permission);
            role.setPermissions(dbPermissions);
        }
        role.setId(req.getId());
        role.setName(req.getName());
        role.setDescription(req.getDescription());
        role.setActive(req.isActive());
        role.setPermissions(role.getPermissions());
        return roleRepository.save(role);
    }

    public void deleteRole(Long id) throws IdInvalidException {
        Role role = getRoleById(id);
        if(role == null) {
            throw new IdInvalidException("Role not found");
        }
        roleRepository.delete(role);
    }

    public ResultPaginationDTO getRole(Specification<Role> spec, Pageable pageable) {
        Page<Role> pageUsers = roleRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageUsers.getTotalPages());
        meta.setTotal(pageUsers.getTotalElements());
        rs.setMeta(meta);
        rs.setResult(pageUsers.getContent());
        return rs;
    }

}
