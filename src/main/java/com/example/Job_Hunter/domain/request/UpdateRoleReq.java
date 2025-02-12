package com.example.Job_Hunter.domain.request;

import com.example.Job_Hunter.domain.Entity.Permission;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class UpdateRoleReq {
    private Long id;
    private String name;
    private String description;
    private boolean active;
    private List<Permission> permissions;
}
