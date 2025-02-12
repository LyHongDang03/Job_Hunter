package com.example.Job_Hunter.service;

import com.example.Job_Hunter.domain.Entity.Permission;
import com.example.Job_Hunter.domain.request.UpdatePermissionReq;
import com.example.Job_Hunter.domain.response.ResultPaginationDTO;
import com.example.Job_Hunter.repository.PermissionRepository;
import com.example.Job_Hunter.utill.exception.IdInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }
    public Permission createPermission(Permission permission) throws IdInvalidException {
        if(permissionRepository.existsByModuleAndApiPathAndMethod(permission.getModule(),
                permission.getApiPath(),
                permission.getMethod())) {
            throw new IdInvalidException("Permission already exists");
        }
        return permissionRepository.save(permission);
    }
    public Permission getPermissionById(Long id) {
        Optional<Permission> permission = permissionRepository.findById(id);
        return permission.orElse(null);
    }
    public ResultPaginationDTO getAllPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pageUsers = permissionRepository.findAll(spec, pageable);
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
    public Permission updatePermission(UpdatePermissionReq req) throws IdInvalidException {
        Permission permission = getPermissionById(req.getId());
        if(permission == null) {
            throw new IdInvalidException("Permission not found");
        }
        if (permissionRepository.existsByModuleAndApiPathAndMethod(req.getModule(),req.getApiPath(),req.getMethod())) {
            throw new IdInvalidException("Permission already exists");
        }
        permission.setName(req.getName());
        permission.setApiPath(req.getApiPath());
        permission.setMethod(req.getMethod());
        permission.setModule(req.getModule());
        return permissionRepository.save(permission);
    }

    public void deletePermission(Long id) throws IdInvalidException {
        Optional<Permission> permission = permissionRepository.findById(id);
        if(permission == null) {
            throw new IdInvalidException("Permission not found");
        }
        Permission permissionCurrent = permission.get();
        permissionCurrent.getRoles().forEach(role -> {
            role.getPermissions().remove(permissionCurrent);
        });
        permissionRepository.deleteById(id);
    }
}
